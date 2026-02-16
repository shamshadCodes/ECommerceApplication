# API Gateway

Single Entry Point for E-Commerce Backend Microservices

## Overview

The API Gateway serves as the single entry point for all client requests to the e-commerce backend system. It routes requests to the appropriate microservices and provides cross-cutting concerns like CORS, logging, and request/response transformation.

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Gateway**: Spring Cloud Gateway
- **Language**: Java 17
- **Build Tool**: Maven

## Port

- **8080** - Single entry point for all services

## Architecture

```
Client → API Gateway (8080) → Microservices
                              ├── UserService (8071)
                              ├── ProductService (8072)
                              ├── InventoryService (8081)
                              ├── OrderService (8083)
                              └── CartService (8084)
```

## Route Configuration

All routes are prefixed with `/api` and automatically routed to the appropriate service:

### User Service Routes
```
Gateway: http://localhost:8080/api/users/**
Routes to: http://localhost:8071/**
```

**Examples:**
- `GET http://localhost:8080/api/users/123` → UserService

### Product Service Routes
```
Gateway: http://localhost:8080/api/products/**
Routes to: http://localhost:8072/products/**
```

**Examples:**
- `GET http://localhost:8080/api/products` → ProductService
- `GET http://localhost:8080/api/products/123` → ProductService
- `POST http://localhost:8080/api/products` → ProductService

### Inventory Service Routes
```
Gateway: http://localhost:8080/api/inventory/**
Routes to: http://localhost:8081/api/v1/inventory/**
```

**Examples:**
- `GET http://localhost:8080/api/inventory/prod123` → InventoryService
- `POST http://localhost:8080/api/inventory/prod123/stock/add` → InventoryService

### Order Service Routes
```
Gateway: http://localhost:8080/api/orders/**
Routes to: http://localhost:8083/api/v1/orders/**
```

**Examples:**
- `POST http://localhost:8080/api/orders` → OrderService
- `GET http://localhost:8080/api/orders/123` → OrderService
- `GET http://localhost:8080/api/orders/user/user123` → OrderService
- `PUT http://localhost:8080/api/orders/123/status` → OrderService

### Cart Service Routes
```
Gateway: http://localhost:8080/api/cart/**
Routes to: http://localhost:8084/api/v1/cart/**
```

**Examples:**
- `POST http://localhost:8080/api/cart` → CartService
- `GET http://localhost:8080/api/cart/user/user123` → CartService
- `POST http://localhost:8080/api/cart/items` → CartService
- `POST http://localhost:8080/api/cart/checkout` → CartService

## Features

### 1. **Centralized Routing**
- Single entry point for all microservices
- Automatic request routing based on path patterns
- Path rewriting for service-specific endpoints

### 2. **CORS Support**
- Global CORS configuration
- Allows all origins (configurable for production)
- Supports all HTTP methods

### 3. **Request Logging**
- Logs all incoming requests
- Logs response status codes
- Helps with debugging and monitoring

### 4. **Load Balancing Ready**
- Can be configured with service discovery
- Supports multiple instances of each service

## Setup

1. **Ensure all microservices are running:**
   - UserService on port 8071
   - ProductService on port 8072
   - InventoryService on port 8081
   - OrderService on port 8083
   - CartService on port 8084

2. **Run the API Gateway:**
```bash
cd APIGateway
./mvnw spring-boot:run
```

3. **Access services through the gateway:**
```bash
# Instead of: http://localhost:8083/api/v1/orders
# Use: http://localhost:8080/api/orders
```

## Health Check

```
GET http://localhost:8080/actuator/health
```

## Gateway Metrics

```
GET http://localhost:8080/actuator/gateway/routes
```

This endpoint shows all configured routes and their status.

## Configuration

The gateway configuration is in `application.yml`:
- Routes are defined under `spring.cloud.gateway.routes`
- Each route has an ID, URI, predicates, and filters
- CORS is configured globally

## Benefits

1. **Simplified Client Integration** - Clients only need to know one URL
2. **Security** - Can add authentication/authorization at gateway level
3. **Monitoring** - Centralized logging and metrics
4. **Flexibility** - Easy to add/remove services without client changes
5. **Load Balancing** - Can distribute requests across service instances

## Future Enhancements

- [ ] Add authentication/authorization filters
- [ ] Implement rate limiting
- [ ] Add circuit breaker pattern
- [ ] Integrate with service discovery (Eureka)
- [ ] Add request/response caching
- [ ] Implement API versioning

## Author

Shamshad Khan - MS in Computer Science Project

