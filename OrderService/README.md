# Order Service

Order Management Service for E-Commerce Backend System

## Overview

The Order Service handles all order-related operations including order creation, status management, and order history tracking. It integrates with the Inventory Service to manage stock levels during order placement and cancellation.

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL
- **ORM**: Spring Data JPA / Hibernate

## Port

- **8083**

## Database

- **Database Name**: `order_db`
- **Tables**: 
  - `orders` - Order information
  - `order_items` - Order line items

## API Endpoints

### Create Order
```
POST /api/v1/orders
```
Creates a new order and reduces inventory stock.

### Get Order by ID
```
GET /api/v1/orders/{id}
```
Retrieves order details by order ID.

### Get Orders by User
```
GET /api/v1/orders/user/{userId}
```
Retrieves all orders for a specific user.

### Get All Orders
```
GET /api/v1/orders
```
Retrieves all orders (admin function).

### Update Order Status
```
PUT /api/v1/orders/{id}/status
```
Updates the status of an order.

### Cancel Order
```
DELETE /api/v1/orders/{id}
```
Cancels an order and restores inventory stock.

### Get Orders by Status
```
GET /api/v1/orders/status/{status}
```
Retrieves orders filtered by status.

### Get Order Count by User
```
GET /api/v1/orders/user/{userId}/count
```
Returns the total number of orders for a user.

## Order Status Flow

1. **PENDING** - Order created, awaiting confirmation
2. **CONFIRMED** - Order confirmed, ready for processing
3. **PROCESSING** - Order being prepared
4. **SHIPPED** - Order shipped to customer
5. **DELIVERED** - Order delivered successfully
6. **CANCELLED** - Order cancelled

## Inter-Service Communication

- **Inventory Service**: 
  - Check stock availability before order creation
  - Reduce stock when order is placed
  - Restore stock when order is cancelled

## Setup

1. Create database:
```sql
CREATE DATABASE order_db;
```

2. Update `application.properties` with your database credentials

3. Run the application:
```bash
./mvnw spring-boot:run
```

## Health Check

```
GET http://localhost:8083/actuator/health
```

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- MySQL Connector
- Spring Boot Starter Validation
- Lombok
- Spring Boot Starter Actuator

## Author

Shamshad Khan - MS in Computer Science Project

