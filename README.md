# Explore With Me

## 🎯 What this project is for

**Explore With Me** is an application that allows users to share information about interesting events and helps find companions to attend them.

The project is implemented as **two microservices** with separate databases:
- **Main service** — contains the core business logic of the application.
- **Statistics service** — stores and provides view data.

---

## ⚙️ Project deployment instructions

```bash
# 1. Clone this repository
git clone <repository_URL>

# 2. Clean the project
mvn clean

# 3. Build the project
mvn package

# 4. Build Docker images
docker-compose build

# 5. Start containers
docker-compose up -d

**Main service**
Contains everything needed for the product to function:

View events without authentication
Create and manage categories
Create and moderate events
Handle user participation requests for events:
create request
confirm
reject
Create and manage event compilations

**Statistics статистики**
Stores view counts and provides various data selections for analyzing application performance.

**Service description**
Main service

Runs on port 8080

The API is divided into three parts:

1️⃣ Public API (available to all users)
Event browsing
Category browsing
Event compilation browsing
2️⃣ Private API (available only to registered users)
Event management
Handling the current user’s participation requests
3️⃣ Admin API (available only to administrators)
Event management
Category management
User management
Event compilation management
Административный API (для администратора проекта)
Работа со статистикой посещений

**Statistics service**
Runs on port 9090
Collects information about:
number of user requests to event lists
number of requests for detailed event information
Based on this data, application usage statistics are generated.

Admin API (project administrator only)
Access to visit statistics
