# E-Commerce Backend Microservices

A comprehensive e-commerce backend system built with Spring Boot microservices architecture, developed as part of an MS in Computer Science degree project.

## 🏗️ Architecture

This project follows a **microservices architecture** with three independent services:

- **ProductService** - Manages product catalog, categories, and inventory
- **UserService** - Handles user authentication, authorization, and session management
- **InventoryService** - Manages stock levels and inventory operations

## 📁 Repository Structure

```
ecommerce-backend/
├── ProductService/          # Product catalog and management
├── UserService/            # User authentication and management
├── InventoryService/       # Inventory and stock management
└── README.md              # This file
```

## 🔄 Monorepo Migration

**Note:** This project was initially developed with separate Git repositories for each microservice. It was migrated to a monorepo structure for:

1. **Development Velocity**: Simplified management as a solo developer
2. **Atomic Commits**: Coordinated changes across services in single commits
3. **Simplified CI/CD**: Unified pipeline with independent service deployment
4. **Academic Submission**: Easier project review and submission

**The migration preserved complete Git history** (121+ commits) from all three services.

## 🚀 Technology Stack

- **Framework**: Spring Boot 3.x
- **Language**: Java 11
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Caching**: Redis
- **Security**: Spring Security, OAuth2

## 🛠️ Getting Started

### Running Individual Services

```bash
# ProductService
cd ProductService && ./mvnw spring-boot:run

# UserService
cd UserService && ./mvnw spring-boot:run

# InventoryService
cd InventoryService && ./mvnw spring-boot:run
```

## 👨‍💻 Author

**Shamshad Khan** - [@shamshadCodes](https://github.com/shamshadCodes)

## 📄 License

Developed for academic purposes as part of an MS in Computer Science degree program.
