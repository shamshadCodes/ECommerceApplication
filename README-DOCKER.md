# 🐳 Docker Setup for E-Commerce Microservices

This guide explains how to run the microservices using Docker.

## 📋 Prerequisites

- Docker Desktop installed and running
- Docker Compose (included with Docker Desktop)
- At least 4GB RAM allocated to Docker

## 🏗️ Architecture

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  UserService    │     │ ProductService  │     │InventoryService │
│   Port: 8071    │────▶│   Port: 8072    │     │   Port: 8081    │
└─────────────────┘     └─────────────────┘     └─────────────────┘
         │                       │                        │
         └───────────────────────┴────────────────────────┘
                                 │
                    ┌────────────┴────────────┐
                    │                         │
              ┌─────▼─────┐           ┌──────▼──────┐
              │   MySQL   │           │    Redis    │
              │ Port: 3306│           │  Port: 6379 │
              └───────────┘           └─────────────┘
```

## 🚀 Quick Start

### 1. Start Only Infrastructure (MySQL + Redis)

```bash
# Start MySQL and Redis only
docker-compose up -d mysql redis

# Check if MySQL is healthy
docker-compose ps
```

### 2. Run Services Locally (Recommended for Development)

After starting MySQL:

```bash
# Terminal 1 - UserService
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
cd UserService
./mvnw spring-boot:run

# Terminal 2 - InventoryService
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
cd InventoryService
./mvnw spring-boot:run

# Terminal 3 - ProductService
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
cd ProductService
./mvnw spring-boot:run
```

### 3. Run Everything in Docker

```bash
# Build and start all services
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build
```

## 📊 Service Status

| Service | Status | Port | Notes |
|---------|--------|------|-------|
| MySQL | ✅ Ready | 3306 | Auto-creates 5 databases |
| Redis | ✅ Ready | 6379 | Optional (ProductService uses cloud Redis) |
| UserService | ✅ Ready | 8071 | Can run in Docker |
| ProductService | ✅ Ready | 8072 | Can run in Docker |
| InventoryService | ✅ Ready | 8081 | Can run in Docker |
| OrderService | ✅ Ready | 8083 | Can run in Docker |
| CartService | ✅ Ready | 8084 | Can run in Docker |
| APIGateway | ✅ Ready | 8080 | Can run in Docker |

## 🗄️ Database Information

### Databases Created Automatically

| Database | User | Password | Service |
|----------|------|----------|---------|
| `user_db` | `userDbUser` | `password` | UserService |
| `catalog_db` | `product_user` | `password` | ProductService |
| `inventoryService` | `root` | `root` | InventoryService |

### Connect to MySQL

```bash
# Using Docker
docker exec -it ecommerce-mysql mysql -uroot -proot

# From host machine
mysql -h 127.0.0.1 -P 3306 -uroot -proot
```

## 🔧 Useful Commands

```bash
# View logs
docker-compose logs -f [service-name]

# Stop all services
docker-compose down

# Stop and remove volumes (deletes database data)
docker-compose down -v

# Rebuild a specific service
docker-compose up -d --build user-service

# Check service health
docker-compose ps

# Access a service shell
docker exec -it user-service sh
```

## 🧪 Testing Services

```bash
# UserService health check
curl http://localhost:8071/actuator/health

# InventoryService health check
curl http://localhost:8081/actuator/health

# ProductService health check (when running)
curl http://localhost:8072/actuator/health
```

## 📝 Environment Variables

You can override environment variables in `docker-compose.yml` or create a `.env` file:

```env
MYSQL_ROOT_PASSWORD=root
MYSQL_PORT=3306
REDIS_PORT=6379
USER_SERVICE_PORT=8071
PRODUCT_SERVICE_PORT=8072
INVENTORY_SERVICE_PORT=8081
```

## 🔄 Development Workflow

1. **Start infrastructure**: `docker-compose up -d mysql redis`
2. **Run services locally** for faster development (hot reload)
3. **Test in Docker** before committing changes
4. **Stop infrastructure**: `docker-compose down`

## 🆘 Troubleshooting

### MySQL Connection Refused

```bash
# Check if MySQL is running
docker-compose ps mysql

# Check MySQL logs
docker-compose logs mysql

# Restart MySQL
docker-compose restart mysql
```

### Service Won't Start

```bash
# Check logs
docker-compose logs [service-name]

# Rebuild without cache
docker-compose build --no-cache [service-name]
```

### Port Already in Use

```bash
# Find process using port
lsof -i :8071

# Kill the process
kill -9 <PID>
```

## 📚 Next Steps

1. ✅ All services are ready and tested
2. ✅ Docker Compose orchestration configured
3. ⬜ Add comprehensive integration tests
4. ⬜ Set up CI/CD pipeline
5. ⬜ Deploy to production environment

