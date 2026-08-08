#!/bin/bash

# Default values
DEFAULT_APP_DIR="."
DEFAULT_JAR_FILE="target/hotdeals-0.0.1-SNAPSHOT.jar"
DEFAULT_LOG_FILE="app.log"
DEFAULT_PORT=8080

# Parse command line arguments
APP_DIR=${1:-$DEFAULT_APP_DIR}
JAR_FILE=${2:-$DEFAULT_JAR_FILE}
LOG_FILE=${3:-$DEFAULT_LOG_FILE}
PORT=${4:-$DEFAULT_PORT}

# Print start message
echo "Starting deployment process..."
echo "App Directory: $APP_DIR"
echo "JAR File: $JAR_FILE"
echo "Log File: $LOG_FILE"
echo "Port: $PORT"

# Navigate to application directory
cd $APP_DIR || { echo "Error: Could not navigate to $APP_DIR"; exit 1; }

# Kill existing process on port
echo "Stopping any process running on port $PORT..."
if lsof -t -i :$PORT > /dev/null; then
    kill -9 $(lsof -t -i :$PORT)
    echo "Process on port $PORT killed."
    # Give it a moment to release the port
    sleep 2
fi

# Pull latest changes
echo "Pulling latest changes from git..."
git pull || { echo "Error: Git pull failed"; exit 1; }

# Build the application
echo "Building application..."
mvn clean package -DskipTests || { echo "Error: Maven build failed"; exit 1; }

# Start the application
echo "Starting application..."
nohup java -jar $JAR_FILE > $LOG_FILE 2>&1 &

# Get the PID of the new process
NEW_PID=$!
echo "Application started with PID: $NEW_PID"

# Wait a few seconds to check if the application started successfully
echo "Waiting for application to start..."
sleep 10

# Check if the application is running
if ps -p $NEW_PID > /dev/null; then
    echo "Deployment successful! Application is running."
    echo "Logs are being written to $LOG_FILE"
else
    echo "Error: Application failed to start. Check $LOG_FILE for details."
    exit 1
fi

# Optional: Display the last few lines of the log
echo "Last 10 lines of the log:"
tail -10 $LOG_FILE
