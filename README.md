# hotdeals-backend

![GitHub top language](https://img.shields.io/github/languages/top/halildurmus/hotdeals-backend?style=for-the-badge)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](https://github.com/halildurmus/hotdeals-backend/blob/master/LICENSE)
![Visits](https://visitor-badge.glitch.me/badge?page_id=halildurmus.hotdeals-backend)

> The **Backend** for my **[hotdeals](https://github.com/halildurmus/hotdeals)** app.

## Table of Contents

* [Features](#features)
* [Documentation](#documentation)
* [Getting Started](#getting-started)
    * [Prerequisites](#prerequisites)
    * [Installation](#installation)
* [Roadmap](#roadmap)
* [Code Contributors](#code-contributors)
* [Contributing](#-contributing)
* [Author](#author)
* [License](#-license)

## Features

- REST API
- CRUD (users, deals, comments, categories, stores, reports)
- MongoDB
- Caching using Redis
- Elasticsearch for search operations
- Authentication (using Firebase Authentication)
- Role based access control (using Firebase Authentication and Spring Security)
- Request validation

## Documentation

The API documentation can be found **[here](https://hotdeals-backend.herokuapp.com/swagger-ui)**.

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

- Java 11+
- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- You need to create a [Firebase](https://firebase.google.com) account and
  setup [Firebase Authentication](https://firebase.google.com/products/auth)
  and [Firebase Cloud Messaging](https://firebase.google.com/products/dynamic-links) services.

### Installation

1. Clone the repo using: `git clone https://github.com/halildurmus/hotdeals-backend.git`
2. In the `src/main/resources` directory, open the `application.yaml` file and change the env
   variables to fit your environment.
3. Start the **Docker Desktop**.
4. In the project root directory, run `docker-compose up -d` to start the containers for **MongoDB,
   Redis and Elasticsearch**. This may take a few minutes the first time as the container images
   need to be downloaded.
5. Download the Firebase service account file from the
   [Firebase Console](https://console.firebase.google.com) (Project Settings -> Service accounts)
   and copy it into the `src/main/resources` directory and rename it to `firebase-admin.json`.
6. Run the app using `mvnw spring-boot:run`.
7. Open your browser and navigate to `127.0.0.1:8080/actuator/health`. You should now see the status
   is `UP` on that JSON response.


## Docker stuff

Sure, let me explain the MongoDB part:

```yaml
mongodb:
  container_name: hotdeals_mongodb
  environment:
    - MONGO_INITDB_DATABASE=db
  image: mongo:6.0
  ports:
    - 27017:27017
  volumes:
    - ./mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js:ro
    - mongodb-data:/data/db
  restart: unless-stopped
  healthcheck:
    test: ["CMD", "mongosh", "--eval", "'db.adminCommand(\"ping\")'"]
    interval: 30s
    timeout: 10s
    retries: 3
  mem_limit: 512m
```

Here's what each part does:

2. `volumes` - I added a second volume mount:
   - `mongodb-data:/data/db` - This creates a named volume to persist your database data. Without this, if your container is removed, all your data would be lost.

3. `restart: unless-stopped` - Makes the container automatically restart if it crashes or if Docker restarts, unless you explicitly stop it.

4. `healthcheck` - Regularly checks if MongoDB is working properly:
   - `test: ["CMD", "mongosh", "--eval", "'db.adminCommand(\"ping\")'"]` - Runs a simple ping command to verify MongoDB is responding
   - `interval: 30s` - Performs this check every 30 seconds
   - `timeout: 10s` - Allows 10 seconds for the check to complete
   - `retries: 3` - Tries the check 3 times before marking the container as unhealthy

5. `mem_limit: 512m` - Sets a memory limit of 512MB for the container to prevent it from consuming too much system memory.


To check the status of services with healthchecks, you have a few different methods:

1. Using `docker ps` to view container health status:
   ```bash
   docker ps
   ```
   This will show a column called "STATUS" that indicates the health status (healthy, unhealthy, or starting).

2. For more detailed health information:
   ```bash
   docker inspect --format='{{json .State.Health}}' hotdeals_mongodb
   ```
   This will show the full health check history, including recent results and timestamps.

3. To see formatted health status for all containers:
   ```bash
   docker ps --format "table {{.Names}}\t{{.Status}}"
   ```

4. Using Docker Compose (if you're using a docker-compose.yml file):
   ```bash
   docker-compose ps
   ```

5. For continuous monitoring:
   ```bash
   watch -n 5 "docker ps --format \"table {{.Names}}\t{{.Status}}\""
   ```
   This will refresh the health status every 5 seconds.

If a container shows as "unhealthy," you can check the logs to see what's wrong:
```bash
docker logs hotdeals_mongodb
```

The healthchecks will run automatically based on the interval you specified (30 seconds in this case), and Docker will update the health status accordingly.

docker stop $(docker ps -a -q) - stop containers  
docker rm $(docker ps -a -q) - remove all the containers  
docker-compose down -v - remove all volumes  
sudo kill -9 $(sudo lsof -t -i:8080)  kill port 8080  
mvn package -Dmaven.test.skip  

docker rm $(docker ps -a -q)  
docker rmi $(docker images -a -q)  
docker volume prune -f  
docker network prune   
docker-compose -p hotdeals up -d  