# Inventory Service

Inventory and Stock Management Service for E-Commerce Backend System

## Overview

The Inventory Service handles all inventory-related operations including stock level management, stock updates, stock availability checks, and inventory tracking. It provides real-time stock information to other services and ensures accurate inventory management across the e-commerce platform.

## Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL
- **ORM**: Spring Data JPA / Hibernate

## Port

- **8081**

## Database

- **Database Name**: `inventoryService`
- **Tables**: 
  - `inventory` - Stock levels and product inventory
  - `inventory_transactions` - Inventory movement history

## API Endpoints

### Get Inventory by Product ID
```
GET /api/v1/inventory/{productId}
```
Retrieves inventory information for a specific product.

**Response:**
```json
{
  "productId": "prod123",
  "quantity": 100,
  "reservedQuantity": 10,
  "availableQuantity": 90,
  "lastUpdated": "2024-01-15T10:30:00"
}
```

### Check Stock Availability
```
GET /api/v1/inventory/{productId}/available?quantity={quantity}
```
Checks if sufficient stock is available for a product.

**Response:**
```json
{
  "available": true,
  "productId": "prod123",
  "requestedQuantity": 5,
  "availableQuantity": 90
}
```

### Add Stock
```
POST /api/v1/inventory/{productId}/stock/add
```
Increases stock quantity for a product.

**Request Body:**
```json
{
  "quantity": 50,
  "reason": "New shipment received"
}
```

### Reduce Stock
```
POST /api/v1/inventory/{productId}/stock/reduce
```
Decreases stock quantity for a product (used during order placement).

**Request Body:**
```json
{
  "quantity": 5,
  "reason": "Order placed"
}
```

### Update Stock
```
PUT /api/v1/inventory/{productId}
```
Updates inventory information for a product.

### Get All Inventory
```
GET /api/v1/inventory
```
Retrieves inventory information for all products.

### Get Low Stock Products
```
GET /api/v1/inventory/low-stock?threshold={threshold}
```
Retrieves products with stock below the specified threshold.

### Reserve Stock
```
POST /api/v1/inventory/{productId}/reserve
```
Reserves stock for pending orders.

### Release Reserved Stock
```
POST /api/v1/inventory/{productId}/release
```
Releases reserved stock (e.g., when order is cancelled).

## Features

- **Real-Time Stock Tracking**: Accurate, up-to-date inventory levels
- **Stock Availability Checks**: Validate stock before order placement
- **Inventory Transactions**: Complete audit trail of stock movements
- **Low Stock Alerts**: Identify products needing restock
- **Stock Reservation**: Reserve stock for pending orders
- **Concurrent Updates**: Handle simultaneous stock updates safely
- **Validation**: Prevent negative stock levels

## Inter-Service Communication

The Inventory Service is called by:
- **Cart Service**: Check stock availability before adding items to cart
- **Order Service**: Reduce stock when orders are placed, restore stock when orders are cancelled
- **Product Service**: Sync stock quantities with product catalog

## Setup

1. Create database:
```sql
CREATE DATABASE inventoryService;
```

2. Update `application.properties` with your database credentials

3. Run the application:
```bash
./mvnw spring-boot:run
```

## Health Check

```
GET http://localhost:8081/actuator/health
```

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- MySQL Connector
- Spring Boot Starter Validation
- Lombok
- Spring Boot Starter Actuator

## Business Rules

- Stock quantity cannot be negative
- Reserved stock is deducted from available quantity
- Stock updates are logged for audit purposes
- Low stock threshold is configurable per product
- Concurrent stock updates use optimistic locking

## Author

Shamshad Khan - MS in Computer Science Project

