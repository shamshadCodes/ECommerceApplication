# Product Service

Product Catalog Management Service for E-Commerce Backend System

## Overview

The Product Service handles all product-related operations including product catalog management, categories, product search, and product information retrieval. It provides comprehensive CRUD operations for managing the e-commerce product inventory and integrates with Redis for caching frequently accessed product data.

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL
- **ORM**: Spring Data JPA / Hibernate
- **Cache**: Redis
- **Validation**: Jakarta Validation

## Port

- **8072**

## Database

- **Database Name**: `catalog_db`
- **Tables**:
  - `products` - Product information
  - `categories` - Product categories
  - `product_images` - Product image URLs

## API Endpoints

### Get All Products
```
GET /products
```
Retrieves all products in the catalog.

**Response:**
```json
[
  {
    "id": "prod123",
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "category": "Electronics",
    "stockQuantity": 50,
    "imageUrl": "https://example.com/laptop.jpg"
  }
]
```

### Get Product by ID
```
GET /products/{id}
```
Retrieves detailed information about a specific product.

### Create Product
```
POST /products
```
Creates a new product in the catalog.

**Request Body:**
```json
{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "category": "Electronics",
  "stockQuantity": 50,
  "imageUrl": "https://example.com/laptop.jpg"
}
```

### Update Product
```
PUT /products/{id}
```
Updates an existing product's information.

### Delete Product
```
DELETE /products/{id}
```
Removes a product from the catalog.

### Search Products
```
GET /products/search?keyword={keyword}
```
Searches products by name or description.

### Get Products by Category
```
GET /products/category/{category}
```
Retrieves all products in a specific category.

### Get Product Categories
```
GET /products/categories
```
Retrieves all available product categories.

## Features

- **Product Catalog Management**: Complete CRUD operations for products
- **Category Management**: Organize products by categories
- **Product Search**: Search products by keywords
- **Redis Caching**: Improved performance with cached product data
- **Image Management**: Support for product images
- **Price Management**: Flexible pricing with decimal precision
- **Stock Tracking**: Track available stock quantities
- **Validation**: Input validation for all product data

## Caching Strategy

- **Redis Integration**: Frequently accessed products are cached
- **Cache Invalidation**: Automatic cache updates on product modifications
- **Performance Optimization**: Reduced database queries for popular products

## Setup

1. Create database:
```sql
CREATE DATABASE catalog_db;
```

2. Ensure Redis is running:
```bash
docker run -d -p 6379:6379 redis:latest
```

3. Update `application.properties` with your database and Redis credentials

4. Run the application:
```bash
./mvnw spring-boot:run
```

## Health Check

```
GET http://localhost:8072/actuator/health
```

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Data Redis
- MySQL Connector
- Spring Boot Starter Validation
- Lombok
- Spring Boot Starter Actuator

## Inter-Service Communication

The Product Service provides product information to:
- **Cart Service**: Product details for cart items
- **Order Service**: Product information for order items
- **Inventory Service**: Product data for inventory management

## Author

Shamshad Khan - MS in Computer Science Project