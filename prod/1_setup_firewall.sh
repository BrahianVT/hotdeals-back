#!/bin/bash
# ═══════════════════════════════════════════════════════════
#  STEP 1: Configure Ubuntu Firewall for OCI
#  - Opens ports 80, 443, 22 via iptables
#  - Persists rules across reboots
#  NOTE: You still need to open ports in OCI Console manually
# ═══════════════════════════════════════════════════════════

source "$(dirname "$0")/config.sh"
check_root

print_header "Setting Up Firewall (iptables)"

# ─── Install netfilter-persistent if needed ───────────────
print_info "Installing netfilter-persistent..."
apt update -y
apt install -y iptables netfilter-persistent iptables-persistent || {
    print_error "Failed to install required packages"
    exit 1
}

# ─── Allow SSH (port 22) - IMPORTANT: do this first ───────
print_info "Allowing SSH on port 22..."
iptables -I INPUT 1 -p tcp --dport 22 -j ACCEPT

# ─── Allow HTTP (port 80) ─────────────────────────────────
print_info "Allowing HTTP on port $HTTP_PORT..."
iptables -I INPUT 2 -p tcp --dport "$HTTP_PORT" -j ACCEPT

# ─── Allow HTTPS (port 443) ───────────────────────────────
print_info "Allowing HTTPS on port $HTTPS_PORT..."
iptables -I INPUT 3 -p tcp --dport "$HTTPS_PORT" -j ACCEPT

# ─── Allow established connections ────────────────────────
iptables -I INPUT 4 -m state --state ESTABLISHED,RELATED -j ACCEPT

# ─── Allow loopback ───────────────────────────────────────
iptables -I INPUT 5 -i lo -j ACCEPT

# ─── Save rules ───────────────────────────────────────────
print_info "Saving iptables rules..."
netfilter-persistent save || {
    print_error "Failed to save iptables rules"
    exit 1
}

# ─── Give Java permission to use port 443 ─────────────────
print_info "Granting Java permission to bind to port 443..."
JAVA_PATH=$(readlink -f $(which java))
if [ -z "$JAVA_PATH" ]; then
    print_error "Java not found. Install Java first."
    exit 1
fi
setcap 'cap_net_bind_service=+ep' "$JAVA_PATH"
print_success "Java can now bind to privileged ports"

# ─── Show current rules ───────────────────────────────────
echo ""
print_info "Current iptables INPUT rules:"
iptables -L INPUT --line-numbers -n

echo ""
print_success "Firewall configured successfully!"
print_warning "REMINDER: You must also open ports in OCI Console:"
echo "  1. Login to https://cloud.oracle.com"
echo "  2. Networking > Virtual Cloud Networks > Your VCN"
echo "  3. Security Lists > Add Ingress Rules:"
echo "     - Port 22  (SSH)   | Source: 0.0.0.0/0 | TCP"
echo "     - Port 80  (HTTP)  | Source: 0.0.0.0/0 | TCP"
echo "     - Port 443 (HTTPS) | Source: 0.0.0.0/0 | TCP"
echo ""
print_info "Next step: Run 2_setup_ssl.sh"