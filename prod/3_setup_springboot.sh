#!/bin/bash
# ═══════════════════════════════════════════════════════════
#  STEP 3: Configure Spring Boot for Production HTTPS
#  Run from: /home/ubuntu/hotdeals-back/prod/
#  Usage: sudo ./3_setup_springboot.sh
#
#  Expects application-prod.yml at:
#  /home/ubuntu/hotdeals-back/src/main/resources/application-prod.yml
# ═══════════════════════════════════════════════════════════

source "$(dirname "$0")/config.sh"
check_root

print_header "Configuring Spring Boot for Production"

# ─── Verify application-prod.yml Exists ───────────────────
PROD_YML="${APP_DIR}/src/main/resources/application-prod.yml"

if [ ! -f "$PROD_YML" ]; then
    print_error "application-prod.yml not found at:"
    print_error "$PROD_YML"
    echo ""
    echo "  Your application-prod.yml should include:"
    echo "  server:"
    echo "    port: ${HTTPS_PORT}"
    echo "    ssl:"
    echo "      enabled: true"
    echo "      key-store: ${KEYSTORE_PATH}"
    echo "      key-store-password: ${KEYSTORE_PASSWORD}"
    echo "      key-store-type: PKCS12"
    echo "      key-alias: ${KEYSTORE_ALIAS}"
    echo ""
    exit 1
fi
print_success "application-prod.yml found at: $PROD_YML"

# ─── Create Log Directory ─────────────────────────────────
print_info "Creating log directory..."
mkdir -p "$(dirname "$LOG_FILE")"
chmod 755 "$(dirname "$LOG_FILE")"
print_success "Log directory: $(dirname "$LOG_FILE")"

# ─── Verify Keystore Exists ───────────────────────────────
if [ ! -f "$KEYSTORE_PATH" ]; then
    print_error "Keystore not found at $KEYSTORE_PATH"
    print_error "Run 2_setup_ssl.sh first"
    exit 1
fi
print_success "Keystore found at: $KEYSTORE_PATH"

# ─── Create systemd Service ───────────────────────────────
print_info "Creating systemd service for auto-start..."
SERVICE_FILE="/etc/systemd/system/${APP_NAME}.service"

cat > "$SERVICE_FILE" << EOF
[Unit]
Description=${APP_NAME} Spring Boot Application (Cloudflare Origin CA)
After=network.target
Wants=network-online.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=${APP_DIR}
ExecStart=$(readlink -f $(which java)) -jar -Dspring.profiles.active=${SPRING_PROFILE} ${APP_DIR}/${JAR_FILE}
StandardOutput=append:${LOG_FILE}
StandardError=append:${LOG_FILE}
Restart=on-failure
RestartSec=10
SuccessExitStatus=143
PIDFile=${PID_FILE}

NoNewPrivileges=false
PrivateTmp=true

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl enable "${APP_NAME}.service"
print_success "Systemd service created: ${APP_NAME}.service"

echo ""
print_success "Spring Boot configuration complete!"
print_info "Service commands:"
echo "  sudo systemctl start   ${APP_NAME}"
echo "  sudo systemctl stop    ${APP_NAME}"
echo "  sudo systemctl restart ${APP_NAME}"
echo "  sudo systemctl status  ${APP_NAME}"
echo ""
print_info "Next step: ./4_deploy_prod.sh"