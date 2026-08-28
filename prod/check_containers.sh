#!/bin/bash
# ═══════════════════════════════════════════════════════════
#  HotDeals Production Containers Health & Security Checker
# ═══════════════════════════════════════════════════════════

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "\n${BLUE}====================================================${NC}"
echo -e "${BLUE}   🔍 HOTDEALS DOCKER CONTAINERS HEALTH CHECK       ${NC}"
echo -e "${BLUE}====================================================${NC}\n"

# ─── 1. Check Container Status ─────────────────────────────
echo -e "${YELLOW}1. Checking Docker Containers State...${NC}"

CONTAINERS=("hotdeals_mongodb_prod" "hotdeals_elasticsearch_prod" "hotdeals_redis_prod")

for CONTAINER in "${CONTAINERS[@]}"; do
    STATUS=$(docker inspect --format '{{.State.Status}}' "$CONTAINER" 2>/dev/null)
    HEALTH=$(docker inspect --format '{{if .State.Health}}{{.State.Health.Status}}{{else}}no-healthcheck{{end}}' "$CONTAINER" 2>/dev/null)

    if [ "$STATUS" = "running" ]; then
        if [ "$HEALTH" = "healthy" ] || [ "$HEALTH" = "no-healthcheck" ]; then
            echo -e "  [${GREEN}UP${NC}] $CONTAINER is RUNNING (Health: ${GREEN}$HEALTH${NC})"
        else
            echo -e "  [${YELLOW}STARTING${NC}] $CONTAINER is RUNNING (Health: ${YELLOW}$HEALTH${NC})"
        fi
    else
        echo -e "  [${RED}DOWN${NC}] $CONTAINER is NOT RUNNING (Status: $STATUS)"
    fi
done

# ─── 2. Database Functional Connectivity Checks ────────────
echo -e "\n${YELLOW}2. Testing Live Database Connectivity...${NC}"

# A. MongoDB Check
if docker exec hotdeals_mongodb_prod mongosh --eval "db.adminCommand('ping')" >/dev/null 2>&1; then
    echo -e "  [${GREEN}PASS${NC}] MongoDB (Port 27018): Responding to ping OK"
else
    echo -e "  [${RED}FAIL${NC}] MongoDB (Port 27018): Connection failed"
fi

# B. Elasticsearch Check
ES_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:9200/ 2>/dev/null)
if [ "$ES_CODE" = "200" ]; then
    ES_CLUSTER=$(curl -s http://127.0.0.1:9200/_cluster/health | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    echo -e "  [${GREEN}PASS${NC}] Elasticsearch (Port 9200): Responding (Cluster Status: ${GREEN}${ES_CLUSTER^^}${NC})"
else
    echo -e "  [${RED}FAIL${NC}] Elasticsearch (Port 9200): Connection failed (HTTP $ES_CODE)"
fi

# C. Redis Check
REDIS_PING=$(docker exec hotdeals_redis_prod redis-cli ping 2>/dev/null)
if [ "$REDIS_PING" = "PONG" ]; then
    echo -e "  [${GREEN}PASS${NC}] Redis (Port 6379): Responding to PING -> PONG"
else
    echo -e "  [${RED}FAIL${NC}] Redis (Port 6379): Connection failed"
fi

# ─── 3. Security Port Binding Verification ─────────────────
echo -e "\n${YELLOW}3. Checking Security Bindings (Localhost Only)...${NC}"

PORTS=("9200" "27018" "6379")
SECURE=true

for PORT in "${PORTS[@]}"; do
    BINDING=$(sudo ss -tulpn | grep -w ":$PORT" | awk '{print $5}' | head -n 1)
    if [[ "$BINDING" =~ 127\.0\.0\.1 ]]; then
        echo -e "  [${GREEN}SECURE${NC}] Port $PORT is bound safely to: $BINDING"
    elif [[ -z "$BINDING" ]]; then
        echo -e "  [${YELLOW}OFFLINE${NC}] Port $PORT is not currently listening."
    else
        echo -e "  [${RED}WARNING${NC}] Port $PORT is EXPOSED to: $BINDING (Fix immediately!)"
        SECURE=false
    fi
done

# ─── 4. Resource Usage ─────────────────────────────────────
echo -e "\n${YELLOW}4. Container Resource Usage (RAM & CPU)...${NC}"
docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}" hotdeals_mongodb_prod hotdeals_elasticsearch_prod hotdeals_redis_prod 2>/dev/null

echo -e "\n${BLUE}====================================================${NC}\n"