# E-Commerce Backend Microservices

A comprehensive e-commerce backend system built with Spring Boot microservices architecture, developed as part of an MS in Computer Science degree project.

## 🏗️ Architecture

This project follows a **microservices architecture** with **6 independent services** and an **API Gateway** for centralized routing:

### Core Services (Original)
- **UserService** (Port 8071) - User authentication, authorization, and session management
- **ProductService** (Port 8072) - Product catalog, categories, and management
- **InventoryService** (Port 8081) - Stock levels and inventory operations

### Enhanced Services (New)
- **OrderService** (Port 8083) - Order processing, order history, and status management
- **CartService** (Port 8084) - Shopping cart management and checkout functionality
- **APIGateway** (Port 8080) - Single entry point for all microservices with routing, CORS, and logging

## 📊 System Architecture Diagram

```
                    ┌──────────────────┐
                    │   API Gateway    │
                    │   Port: 8080     │
                    └────────┬─────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
   ┌────▼────┐         ┌────▼────┐         ┌────▼────┐
   │  User   │         │ Product │         │Inventory│
   │ Service │         │ Service │         │ Service │
   │  :8071  │         │  :8072  │         │  :8081  │
   └─────────┘         └─────────┘         └─────────┘
                             │                    │
                        ┌────▼────┐         ┌────▼────┐
                        │  Order  │◄────────┤  Cart   │
                        │ Service │         │ Service │
                        │  :8083  │         │  :8084  │
                        └─────────┘         └─────────┘
```

## 📁 Repository Structure

```
ecommerce-backend/
├── UserService/            # User authentication and management (Port 8071)
├── ProductService/         # Product catalog and management (Port 8072)
├── InventoryService/       # Inventory and stock management (Port 8081)
├── OrderService/           # Order processing and management (Port 8083) ⭐ NEW
├── CartService/            # Shopping cart and checkout (Port 8084) ⭐ NEW
├── APIGateway/             # API Gateway for routing (Port 8080) ⭐ NEW
├── docs/                   # Documentation
│   ├── IMPLEMENTATION_PLAN.md
│   ├── QUICK_START_GUIDE.md
│   └── TESTING_GUIDE.md
└── README.md              # This file
```

## 🔄 Monorepo Migration

**Note:** This project was initially developed with separate Git repositories for each microservice. It was migrated to a monorepo structure for:

1. **Development Velocity**: Simplified management as a solo developer
2. **Atomic Commits**: Coordinated changes across services in single commits
3. **Simplified CI/CD**: Unified pipeline with independent service deployment
4. **Academic Submission**: Easier project review and submission

**The migration preserved complete Git history** (121+ commits) from all three services.

## ✨ Key Features

### E-Commerce Functionality
- ✅ **Product Catalog Management** - Browse, search, and manage products
- ✅ **User Authentication & Authorization** - JWT + OAuth2 with Spring Security
- ✅ **Inventory Management** - Real-time stock tracking and updates
- ✅ **Shopping Cart** - Add, update, remove items with automatic total calculation
- ✅ **Order Processing** - Complete order lifecycle from creation to delivery
- ✅ **Inter-Service Communication** - Seamless data flow between services
- ✅ **API Gateway** - Single entry point with routing and CORS support

### Technical Features
- 🔄 **Microservices Architecture** - Independent, scalable services
- 🗄️ **Database Per Service** - Each service has its own database
- 🔗 **RESTful APIs** - Clean, well-documented REST endpoints
- 🔐 **Security** - Spring Security, JWT tokens, OAuth2 Authorization Server
- 🛡️ **Exception Handling** - Comprehensive error handling across all services
- ✔️ **Validation** - Jakarta validation on all requests
- 📊 **Health Checks** - Actuator endpoints for monitoring
- 🌐 **CORS Support** - Cross-origin resource sharing enabled

## 🚀 Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL 8.0+
- **ORM**: JPA/Hibernate
- **Security**: Spring Security, JWT, OAuth2 Authorization Server
- **Gateway**: Spring Cloud Gateway
- **Validation**: Jakarta Validation
- **Utilities**: Lombok

## 🛠️ Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Database Setup
```sql
CREATE DATABASE user_management_db;
CREATE DATABASE catalog_db;
CREATE DATABASE inventoryService;
CREATE DATABASE order_db;
CREATE DATABASE cart_db;
```

### Running Services

**Option 1: Through API Gateway (Recommended)**

Start all services in this order:
```bash
# 1. Start core services
cd UserService && ./mvnw spring-boot:run &
cd ProductService && ./mvnw spring-boot:run &
cd InventoryService && ./mvnw spring-boot:run &

# 2. Start enhanced services
cd OrderService && ./mvnw spring-boot:run &
cd CartService && ./mvnw spring-boot:run &

# 3. Start API Gateway
cd APIGateway && ./mvnw spring-boot:run
```

Access all services through: `http://localhost:8080/api/`

**Option 2: Direct Service Access**
```bash
# UserService
cd UserService && ./mvnw spring-boot:run

# ProductService
cd ProductService && ./mvnw spring-boot:run

# InventoryService
cd InventoryService && ./mvnw spring-boot:run

# OrderService
cd OrderService && ./mvnw spring-boot:run

# CartService
cd CartService && ./mvnw spring-boot:run
```

## 📡 API Endpoints

### Through API Gateway (Port 8080)
- **Users**: `http://localhost:8080/api/users/**`
- **Products**: `http://localhost:8080/api/products/**`
- **Inventory**: `http://localhost:8080/api/inventory/**`
- **Orders**: `http://localhost:8080/api/orders/**`
- **Cart**: `http://localhost:8080/api/cart/**`

### Direct Service Access
- **UserService**: `http://localhost:8071/**`
- **ProductService**: `http://localhost:8072/products/**`
- **InventoryService**: `http://localhost:8081/api/v1/inventory/**`
- **OrderService**: `http://localhost:8083/api/v1/orders/**`
- **CartService**: `http://localhost:8084/api/v1/cart/**`

## 🧪 Testing

See the comprehensive [Testing Guide](docs/TESTING_GUIDE.md) for:
- Service health checks
- API endpoint testing
- Complete e-commerce workflow
- Inter-service communication tests
- Error handling scenarios

### Quick Test
```bash
# Check all services are running
curl http://localhost:8080/actuator/health

# Browse products
curl http://localhost:8080/api/products

# Add to cart
curl -X POST http://localhost:8080/api/cart/items \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "productId": "prod456",
    "productName": "Sample Product",
    "quantity": 2,
    "price": 99.99
  }'

# Checkout
curl -X POST http://localhost:8080/api/cart/checkout?userId=user123
```

## 📚 Documentation

- **[Quick Start Guide](docs/QUICK_START_GUIDE.md)** - Complete setup and startup instructions
- **[Testing Guide](docs/TESTING_GUIDE.md)** - Comprehensive API testing examples
- **[Implementation Plan](docs/IMPLEMENTATION_PLAN.md)** - Project roadmap and progress tracking
- **[OrderService README](OrderService/README.md)** - Order service documentation
- **[CartService README](CartService/README.md)** - Cart service documentation
- **[APIGateway README](APIGateway/README.md)** - API Gateway documentation

## 🔄 Inter-Service Communication

The system demonstrates sophisticated inter-service communication:

1. **CartService → InventoryService**: Validates stock availability before adding items
2. **CartService → OrderService**: Creates orders during checkout
3. **OrderService → InventoryService**: Reduces stock when orders are created
4. **OrderService → InventoryService**: Restores stock when orders are cancelled

All communication uses **RestTemplate** for synchronous REST calls.

## 🎯 Project Highlights

### Academic Excellence
- ✅ **Complete Microservices Architecture** - 6 independent services
- ✅ **Database Per Service Pattern** - True microservices isolation
- ✅ **API Gateway Pattern** - Centralized routing and cross-cutting concerns
- ✅ **Security Implementation** - Spring Security, JWT, OAuth2 Authorization Server
- ✅ **Inter-Service Communication** - RESTful service-to-service calls
- ✅ **Comprehensive Documentation** - Detailed guides and README files
- ✅ **Production-Ready Code** - Exception handling, validation, logging

### Technical Achievements
- **51+ Java Files** created across new services
- **15+ REST Endpoints** implemented
- **4 Databases** with independent schemas
- **100% Functional** e-commerce workflow
- **80% Project Completion** (4/5 phases complete)

## 🚧 Future Enhancements

### Phase 5: Service Discovery (Optional)
- [ ] Eureka Server for dynamic service registration
- [ ] Client-side load balancing
- [ ] Service health monitoring
- [ ] Dynamic routing in API Gateway

### Additional Features
- [ ] Payment gateway integration (Stripe, PayPal)
- [ ] Email notifications for orders
- [ ] Real-time order tracking system
- [ ] Admin dashboard with analytics
- [ ] Rate limiting at API Gateway
- [ ] Circuit breaker pattern (Resilience4j)
- [ ] Distributed tracing (Zipkin, Sleuth)
- [ ] Caching layer (Redis)
- [ ] Message queue (RabbitMQ, Kafka)

## 🔧 Troubleshooting

### Services won't start
- Check if ports are already in use
- Verify MySQL is running
- Check database credentials in application.properties

### Inter-service communication fails
- Ensure all dependent services are running
- Check service URLs in application.properties
- Verify network connectivity

### Database connection errors
- Verify databases exist
- Check MySQL credentials
- Ensure MySQL is running on port 3306

See [Quick Start Guide](docs/QUICK_START_GUIDE.md) for more troubleshooting tips.

## 👨‍💻 Author

**Shamshad Khan** - [@shamshadCodes](https://github.com/shamshadCodes)

## 📄 License

Developed for academic purposes as part of an MS in Computer Science degree program.

---

**Last Updated**: February 18, 2026
**Project Status**: 80% Complete (4/5 Phases)
**Total Services**: 6 Microservices + 1 API Gateway
