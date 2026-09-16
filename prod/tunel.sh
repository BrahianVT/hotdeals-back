#!/bin/bash
DOMAIN="api.promoabastos.com"
SERVER_IP="141.148.155.223"
HEALTH_PATH="/actuator/health"
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'
ok()   { echo -e "${GREEN}[OK]${NC}  $1"; }
fail() { echo -e "${RED}[FAIL]${NC} $1"; }
echo "=============================="
echo " Cloudflare Tunnel Check"
echo "=============================="
# 1. cloudflared service running?
if systemctl is-active --quiet cloudflared; then
  ok "cloudflared service is running"
else
  fail "cloudflared service is NOT running"
  echo "     → Run: sudo systemctl start cloudflared"
fi
# 2. Tunnel connected to Cloudflare?
CONNECTIONS=$(sudo journalctl -u cloudflared --since "5 minutes ago" --no-pager | grep -c "Registered tunnel connection")
if [ "$CONNECTIONS" -gt 0 ]; then
  ok "Tunnel has active connections ($CONNECTIONS registered recently)"
else
  RUNNING_SINCE=$(systemctl show cloudflared --property=ActiveEnterTimestamp | cut -d= -f2)
  ok "Tunnel running since: $RUNNING_SINCE"
fi
# 3. DNS resolves to Cloudflare (not OCI IP)?
RESOLVED_IP=$(nslookup "$DOMAIN" | awk '/^Address: / { print $2 }' | grep -v '127.0.0.53' | head -1)
if echo "$RESOLVED_IP" | grep -qE '^(104\.|172\.6[4-9]\.|172\.7[0-1]\.|198\.41\.)'; then
  ok "DNS resolves to Cloudflare IP: $RESOLVED_IP"
elif [ "$RESOLVED_IP" = "$SERVER_IP" ]; then
  fail "DNS still points to OCI IP $SERVER_IP — tunnel DNS not active!"
else
  ok "DNS resolved: $RESOLVED_IP"
fi
# 4. Domain reachable via Cloudflare?
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "https://$DOMAIN$HEALTH_PATH")
CF_HEADER=$(curl -sI --max-time 5 "https://$DOMAIN$HEALTH_PATH" | grep -i "^server:" | tr -d '\r')
if [[ "$HTTP_CODE" == "200" || "$HTTP_CODE" == "401" || "$HTTP_CODE" == "403" ]]; then
  ok "Domain responding (HTTP $HTTP_CODE) — $CF_HEADER"
else