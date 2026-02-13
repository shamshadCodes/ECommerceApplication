# Cart Service

Shopping Cart Management Service for E-Commerce Backend System

## Overview

The Cart Service handles all shopping cart operations including adding items, updating quantities, removing items, and checkout functionality. It integrates with the Inventory Service to check stock availability and the Order Service to create orders during checkout.

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL
- **ORM**: Spring Data JPA / Hibernate

## Port

- **8084**

## Database

- **Database Name**: `cart_db`
- **Tables**: 
  - `carts` - Shopping cart information
  - `cart_items` - Cart line items

## API Endpoints

### Create Cart
```
POST /api/v1/cart?userId={userId}
```
Creates a new shopping cart for a user.

### Get Cart by User
```
GET /api/v1/cart/user/{userId}
```
Retrieves the shopping cart for a specific user.

### Add Item to Cart
```
POST /api/v1/cart/items
```
Adds a product to the shopping cart or updates quantity if already exists.

**Request Body:**
```json
{
  "userId": "user123",
  "productId": "prod456",
  "productName": "Product Name",
  "quantity": 2,
  "price": 99.99
}
```

### Update Cart Item
```
PUT /api/v1/cart/items/{itemId}
```
Updates the quantity of a cart item.

**Request Body:**
```json
{
  "quantity": 5
}
```

### Remove Cart Item
```
DELETE /api/v1/cart/items/{itemId}
```
Removes an item from the cart.

### Checkout
```
POST /api/v1/cart/checkout?userId={userId}
```
Converts the cart to an order and clears the cart.

**Response:**
```json
{
  "success": true,
  "message": "Checkout completed successfully",
  "data": "order-id-123",
  "timestamp": "2024-01-15T10:30:00"
}
```

### Clear Cart
```
DELETE /api/v1/cart/{cartId}
```
Removes all items from the cart.

## Inter-Service Communication

- **Inventory Service**: 
  - Check stock availability before adding items to cart
  
- **Order Service**: 
  - Create order from cart during checkout

- **Product Service**: 
  - Validate product information (optional)

## Features

- **Automatic Cart Creation**: Cart is automatically created when first item is added
- **Quantity Management**: Automatically updates quantity if same product is added again
- **Stock Validation**: Checks inventory before adding items
- **Checkout Integration**: Seamlessly converts cart to order
- **Auto-Clear on Checkout**: Cart is automatically cleared after successful checkout

## Setup

1. Create database:
```sql
CREATE DATABASE cart_db;
```

2. Update `application.properties` with your database credentials

3. Run the application:
```bash
./mvnw spring-boot:run
```

## Health Check

```
GET http://localhost:8084/actuator/health
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

