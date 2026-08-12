#!/bin/bash
# ═══════════════════════════════════════════════════════════
#  STEP 4: Deploy Application to Production
#  Run from: /home/ubuntu/hotdeals-back/prod/
#  Usage: ./4_deploy_prod.sh
# ═══════════════════════════════════════════════════════════

source "$(dirname "$0")/config.sh"

print_header "Deploying ${APP_NAME} to Production"
echo "  Domain  : ${DOMAIN}"
echo "  Profile : ${SPRING_PROFILE}"
echo "  Port    : ${HTTPS_PORT} (HTTPS)"
echo "  App Dir : ${APP_DIR}"

# ─── Pre-flight Checks ────────────────────────────────────
print_info "Running pre-flight checks..."

command -v java >/dev/null 2>&1 || { print_error "Java not installed"; exit 1; }
command -v mvn  >/dev/null 2>&1 || { print_error "Maven not installed"; exit 1; }
command -v git  >/dev/null 2>&1 || { print_error "Git not installed"; exit 1; }

[ ! -f "$KEYSTORE_PATH" ] && {
    print_error "Keystore not found: $KEYSTORE_PATH"
    print_error "Run: sudo ./2_setup_ssl.sh first"
    exit 1
}

PROD_YML="${APP_DIR}/src/main/resources/application-prod.yml"
[ ! -f "$PROD_YML" ] && {
    print_error "application-prod.yml not found: $PROD_YML"
    exit 1
}

print_success "All pre-flight checks passed"

# ─── Navigate to App Directory ────────────────────────────
cd "$APP_DIR" || {
    print_error "Cannot navigate to $APP_DIR"
    exit 1
}

# ─── Stop Existing Application ────────────────────────────
print_info "Stopping existing application..."

if [ -f "$PID_FILE" ]; then
    OLD_PID=$(cat "$PID_FILE")
    if ps -p "$OLD_PID" > /dev/null 2>&1; then
        print_info "Gracefully stopping PID: $OLD_PID..."
        kill -15 "$OLD_PID"
        sleep 5
        if ps -p "$OLD_PID" > /dev/null 2>&1; then
            print_warning "Force killing PID: $OLD_PID"
            kill -9 "$OLD_PID"
            sleep 2
        fi
        print_success "Application stopped"
    else
        print_info "PID $OLD_PID not running"
    fi
    rm -f "$PID_FILE"
fi

if fuser "$HTTPS_PORT/tcp" > /dev/null 2>&1; then
    print_warning "Killing remaining process on port $HTTPS_PORT..."
    fuser -k "$HTTPS_PORT/tcp"
    sleep 2
fi

# ─── Git Pull ─────────────────────────────────────────────
print_info "Pulling latest changes from branch: $GIT_BRANCH..."
git fetch origin
git checkout "$GIT_BRANCH"
git pull origin "$GIT_BRANCH" || {
    print_error "Git pull failed"
    exit 1
}

COMMIT_INFO=$(git log -1 --format='%h - %s (%ci)')
print_success "Code updated: $COMMIT_INFO"

# ─── Maven Build ──────────────────────────────────────────
print_info "Building application..."
mvn clean package \
    -DskipTests \
    -Dspring.profiles.active="$SPRING_PROFILE" || {
    print_error "Maven build failed"
    exit 1
}
print_success "Build successful"

# ─── Verify JAR ───────────────────────────────────────────
if [ ! -f "$JAR_FILE" ]; then
    print_error "JAR not found: ${APP_DIR}/${JAR_FILE}"
    exit 1
fi
JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
print_success "JAR verified: $JAR_FILE ($JAR_SIZE)"

# ─── Create Log Dir ───────────────────────────────────────
mkdir -p "$(dirname "$LOG_FILE")"

# ─── Start Application ────────────────────────────────────
print_info "Starting application with HTTPS on port $HTTPS_PORT..."
nohup java \
    -jar "$JAR_FILE" \
    --spring.profiles.active="$SPRING_PROFILE" \
    > "$LOG_FILE" 2>&1 &

NEW_PID=$!
echo "$NEW_PID" > "$PID_FILE"
print_info "Started with PID: $NEW_PID"

# ─── Wait & Health Check ──────────────────────────────────
print_info "Waiting for application to start..."
READY=false
for i in {1..8}; do
    sleep 5
    echo -n "  Attempt $i/8 ($(( i * 5 ))s): "

    if ! ps -p "$NEW_PID" > /dev/null 2>&1; then
        print_error "Process died!"
        break
    fi

    HTTP_CODE=$(curl -sk -o /dev/null -w "%{http_code}" \
        --max-time 5 \
        "https://localhost:${HTTPS_PORT}/actuator/health" 2>/dev/null || echo "000")

    if [ "$HTTP_CODE" = "200" ]; then
        READY=true
        print_success "Application is UP! (HTTP $HTTP_CODE)"
        break
    elif [ "$HTTP_CODE" = "401" ] || [ "$HTTP_CODE" = "403" ]; then
        READY=true
        print_success "Application is UP! (HTTP $HTTP_CODE - auth required)"
        break
    else
        echo "Not ready yet (HTTP $HTTP_CODE)..."
    fi
done

# ─── Final Status ─────────────────────────────────────────
echo ""
if [ "$READY" = true ] && ps -p "$NEW_PID" > /dev/null 2>&1; then
    echo -e "${GREEN}══════════════════════════════════════════════${NC}"
    echo -e "${GREEN}  ✅ DEPLOYMENT SUCCESSFUL!${NC}"
    echo -e "${GREEN}══════════════════════════════════════════════${NC}"
    echo "  URL     : https://${DOMAIN}"
    echo "  PID     : $NEW_PID"
    echo "  Profile : $SPRING_PROFILE"
    echo "  Branch  : $GIT_BRANCH"
    echo "  Commit  : $COMMIT_INFO"
    echo "  Log     : $LOG_FILE"
    echo ""
    print_warning "Don't forget: Cloudflare SSL → Full (strict)"
    echo ""
else
    echo -e "${RED}══════════════════════════════════════════════${NC}"
    echo -e "${RED}  ❌ DEPLOYMENT FAILED!${NC}"
    echo -e "${RED}══════════════════════════════════════════════${NC}"
    echo ""
    echo "Last 30 lines of log:"
    echo "────────────────────────────────────"
    tail -30 "$LOG_FILE"
    exit 1
fi

# ─── Show Last Logs ───────────────────────────────────────
print_info "Last 15 lines of application log:"
echo "────────────────────────────────────"
tail -15 "$LOG_FILE"