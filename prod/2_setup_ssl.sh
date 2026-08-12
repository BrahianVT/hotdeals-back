#!/bin/bash
# ═══════════════════════════════════════════════════════════
#  STEP 2: Setup SSL with Cloudflare Origin CA
#  Run from: /home/ubuntu/hotdeals-back/prod/
#  Usage: sudo ./2_setup_ssl.sh
#
#  PRE-REQUISITES:
#  cert.pem and key.pem must be in:
#  /home/ubuntu/hotdeals-back/prod/cert.pem
#  /home/ubuntu/hotdeals-back/prod/key.pem
# ═══════════════════════════════════════════════════════════

source "$(dirname "$0")/config.sh"
check_root

print_header "Setting Up Cloudflare Origin CA Certificate"

# ─── Verify cert files exist in prod/ ─────────────────────
print_info "Looking for certificate files in: $CERT_UPLOAD_DIR"

if [ ! -f "${CERT_UPLOAD_DIR}/cert.pem" ]; then
    print_error "cert.pem not found in ${CERT_UPLOAD_DIR}"
    echo ""
    echo "  Before running this script, you must:"
    echo "  1. Go to Cloudflare > SSL/TLS > Origin Server"
    echo "  2. Create Certificate (RSA 2048)"
    echo "  3. Add hostnames: ${DOMAIN}, *.${DOMAIN}"
    echo "  4. Save Origin Certificate as: ${CERT_UPLOAD_DIR}/cert.pem"
    echo "  5. Save Private Key as:        ${CERT_UPLOAD_DIR}/key.pem"
    echo ""
    exit 1
fi

if [ ! -f "${CERT_UPLOAD_DIR}/key.pem" ]; then
    print_error "key.pem not found in ${CERT_UPLOAD_DIR}"
    exit 1
fi

print_success "cert.pem found"
print_success "key.pem found"

# ─── Validate certificate ─────────────────────────────────
print_info "Validating certificate..."

openssl x509 -in "${CERT_UPLOAD_DIR}/cert.pem" -noout -text > /dev/null 2>&1 || {
    print_error "cert.pem is not a valid certificate"
    exit 1
}

openssl rsa -in "${CERT_UPLOAD_DIR}/key.pem" -check -noout > /dev/null 2>&1 || {
    print_error "key.pem is not a valid private key"
    exit 1
}

CERT_MD5=$(openssl x509 -noout -modulus -in "${CERT_UPLOAD_DIR}/cert.pem" | openssl md5)
KEY_MD5=$(openssl rsa -noout -modulus -in "${CERT_UPLOAD_DIR}/key.pem" | openssl md5)

if [ "$CERT_MD5" != "$KEY_MD5" ]; then
    print_error "cert.pem and key.pem do NOT match!"
    exit 1
fi

print_success "Certificate and key are valid and match"

CERT_EXPIRY=$(openssl x509 -noout -enddate -in "${CERT_UPLOAD_DIR}/cert.pem" | cut -d= -f2)
CERT_ISSUER=$(openssl x509 -noout -issuer -in "${CERT_UPLOAD_DIR}/cert.pem" | sed 's/issuer=//')
echo "  Issuer  : $CERT_ISSUER"
echo "  Expires : $CERT_EXPIRY"

# ─── Move certs to secure directory ───────────────────────
print_info "Moving certificates to ${CERTS_DIR}..."
mkdir -p "$CERTS_DIR"
cp "${CERT_UPLOAD_DIR}/cert.pem" "$CERT_FILE"
cp "${CERT_UPLOAD_DIR}/key.pem" "$KEY_FILE"

chmod 644 "$CERT_FILE"
chmod 600 "$KEY_FILE"
print_success "Certificates stored securely in ${CERTS_DIR}"

# ─── Create PKCS12 Keystore ───────────────────────────────
print_info "Creating PKCS12 keystore for Spring Boot..."
openssl pkcs12 -export \
    -in "$CERT_FILE" \
    -inkey "$KEY_FILE" \
    -out "$KEYSTORE_PATH" \
    -name "$KEYSTORE_ALIAS" \
    -passout "pass:${KEYSTORE_PASSWORD}" || {
    print_error "Failed to create keystore"
    exit 1
}

chmod 644 "$KEYSTORE_PATH"
print_success "Keystore created at: $KEYSTORE_PATH"

# ─── Summary ──────────────────────────────────────────────
echo ""
print_success "Cloudflare Origin CA Setup Complete!"
echo ""
echo "  Certificate  : $CERT_FILE"
echo "  Private Key  : $KEY_FILE"
echo "  Keystore     : $KEYSTORE_PATH"
echo "  Expires      : $CERT_EXPIRY"
echo ""
print_warning "IMPORTANT - Cloudflare DNS Setup:"
echo "  1. Cloudflare > DNS > Add A record:"
echo "     Name: @  |  Value: $SERVER_IP  |  Proxy: ON"
echo "  2. After deploying, set SSL mode to Full (strict)"
echo ""
print_info "Next step: sudo ./3_setup_springboot.sh"