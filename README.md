# hotdeals-backend

![GitHub top language](https://img.shields.io/github/languages/top/halildurmus/hotdeals-backend?style=for-the-badge)
[![GitHub contributors](https://img.shields.io/github/contributors-anon/halildurmus/hotdeals-backend?style=for-the-badge)](https://github.com/halildurmus/hotdeals-backend/graphs/contributors)
[![GitHub issues](https://img.shields.io/github/issues/halildurmus/hotdeals-backend?style=for-the-badge)](https://github.com/halildurmus/hotdeals-backend/issues)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](https://github.com/halildurmus/hotdeals-backend/blob/master/LICENSE)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-blue?logo=linkedin&labelColor=blue&style=for-the-badge)](https://linkedin.com/in/halildurmus)
![Visits](https://badges.pufler.dev/visits/halildurmus/hotdeals-backend?style=for-the-badge)

> This is the **Backend** for my **[hotdeals app](https://github.com/halildurmus/hotdeals-app)**.

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

**TODO**

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites
- Java 11+
- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- You need to create a **[Firebase](https://firebase.google.com)** account and setup **[Firebase Authentication](https://firebase.google.com/products/auth)** and **[Firebase Cloud Messaging](https://firebase.google.com/products/dynamic-links)** services.

### Installation

1. Clone the repo using: `git clone https://github.com/halildurmus/hotdeals-backend.git`
2. In the `src/main/resources` directory, copy the `application-example.yaml` file and rename it to `application.yaml`.
3. Open the `application.yaml` file and change the env variables to fit your environment.
4. Start the **Docker Desktop**.
5. In the project root directory, run `docker-compose up -d` to start the containers for **MongoDB, Redis and Elasticsearch**.
   This may take a few minutes the first time as the container images need to be downloaded.
6. Download the configuration file from the [Firebase Console](https://console.firebase.google.com) (google-services.json) and copy it into the `src/main/resources` directory.
7. Set the environment variable `GOOGLE_APPLICATION_CREDENTIALS` to the file path of the `google-services.json` file.
    * On Windows, run: `set GOOGLE_APPLICATION_CREDENTIALS=C:\Users\username\Desktop\hotdeals-backend\src\main\resources\google-services.json`.
    * On Linux or macOS, run: `export GOOGLE_APPLICATION_CREDENTIALS="/home/user/Downloads/google-services.json"`.
8. Run the app using `mvnw spring-boot:run`.
9. Open your browser and navigate to `localhost:8080/actuator/health`. You should now see the status is `UP` on that JSON response.

## Roadmap

See the [open issues](https://github.com/halildurmus/hotdeals-backend/issues) for a list of proposed features (and known issues).

## Code Contributors

This project exists thanks to all the people who contribute.

<a href="https://github.com/halildurmus/hotdeals-backend/graphs/contributors">
  <img class="avatar" alt="halildurmus" src="https://github.com/halildurmus.png?v=4&s=96" width="48" height="48" />
</a>

## 🤝 Contributing

Contributions, issues and feature requests are welcome.  
Feel free to check [issues page](https://github.com/halildurmus/hotdeals-backend/issues) if you want to contribute.

## Author

👤 **Halil İbrahim Durmuş**

- Github: [@halildurmus](https://github.com/halildurmus)

## 📝 License

This project is [MIT](https://github.com/halildurmus/hotdeals-backend/blob/master/LICENSE) licensed.