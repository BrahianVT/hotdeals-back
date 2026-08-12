#!/bin/bash
# ═══════════════════════════════════════════════════════════
#  SHARED CONFIGURATION - Edit these variables before running
#  Base path: /home/ubuntu/hotdeals-back/
# ═══════════════════════════════════════════════════════════

# ─── Application ──────────────────────────────────────────
APP_NAME="hotdeals"
APP_VERSION="0.0.1-SNAPSHOT"
APP_DIR="/home/ubuntu/hotdeals-back"
JAR_FILE="target/${APP_NAME}-${APP_VERSION}.jar"
LOG_FILE="/var/log/${APP_NAME}/app.log"
PID_FILE="/var/run/${APP_NAME}.pid"
SPRING_PROFILE="prod"

# ─── Server / Network ─────────────────────────────────────
SERVER_IP="132.x.x.x"                                   # Your OCI public IP
DOMAIN="promoabastos.com"
HTTP_PORT=80
HTTPS_PORT=443

# ─── SSL / Cloudflare Origin CA ───────────────────────────
CERTS_DIR="/var/certs"
CERT_FILE="${CERTS_DIR}/cert.pem"
KEY_FILE="${CERTS_DIR}/key.pem"
KEYSTORE_PATH="${CERTS_DIR}/keystore.p12"
KEYSTORE_PASSWORD=""               # void for now
KEYSTORE_ALIAS="tomcat"

# Upload source: where cert.pem and key.pem live in the repo
CERT_UPLOAD_DIR="${APP_DIR}/prod"

# ─── Scripts Directory ────────────────────────────────────
SCRIPTS_DIR="${APP_DIR}/prod"

# ─── Git ──────────────────────────────────────────────────
GIT_BRANCH="main"

# ─── Colors for output ────────────────────────────────────
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# ─── Helper Functions ─────────────────────────────────────
print_header() {
    echo -e "\n${BLUE}══════════════════════════════════════${NC}"
    echo -e "${BLUE}  $1${NC}"
    echo -e "${BLUE}══════════════════════════════════════${NC}\n"
}

print_success() { echo -e "${GREEN}✅ $1${NC}"; }
print_error()   { echo -e "${RED}❌ $1${NC}"; }
print_warning() { echo -e "${YELLOW}⚠️  $1${NC}"; }
print_info()    { echo -e "${BLUE}ℹ️  $1${NC}"; }

check_root() {
    if [ "$EUID" -ne 0 ]; then
        print_error "Please run as root: sudo $0"
        exit 1
    fi
}