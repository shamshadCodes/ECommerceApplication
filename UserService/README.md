# User Service

User Authentication and Authorization Service for E-Commerce Backend System

## Overview

The User Service handles all user-related operations including user registration, authentication, authorization, and session management. It provides secure JWT-based authentication and OAuth2 authorization server capabilities for the entire e-commerce platform.

## Technology Stack

- **Framework**: Spring Boot 3.2.1
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security, JWT, OAuth2 Authorization Server

## Port

- **8071**

## Database

- **Database Name**: `user_management_db`
- **Tables**:
  - `users` - User account information
  - `roles` - User roles and permissions
  - `user_sessions` - Active user sessions

## API Endpoints

### User Registration
```
POST /api/users/register
```
Registers a new user account.

**Request Body:**
```json
{
  "username": "john.doe",
  "email": "john@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe"
}
```

### User Login
```
POST /api/users/login
```
Authenticates a user and returns JWT token.

**Request Body:**
```json
{
  "username": "john.doe",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "user123",
  "username": "john.doe",
  "expiresIn": 3600
}
```

### Get User Profile
```
GET /api/users/{userId}
```
Retrieves user profile information.

### Update User Profile
```
PUT /api/users/{userId}
```
Updates user profile information.

### Get All Users
```
GET /api/users
```
Retrieves all users (admin function).

### Delete User
```
DELETE /api/users/{userId}
```
Deletes a user account.

## Features

- **JWT Authentication**: Secure token-based authentication
- **OAuth2 Authorization Server**: Centralized authorization for all services
- **Password Encryption**: BCrypt password hashing
- **Role-Based Access Control**: Support for user roles and permissions
- **Session Management**: Track active user sessions
- **User Profile Management**: Complete CRUD operations for user accounts

## Security

- **Spring Security**: Comprehensive security framework
- **JWT Tokens**: Stateless authentication with JSON Web Tokens
- **OAuth2**: Authorization server for microservices
- **Password Hashing**: BCrypt with configurable strength
- **CORS Support**: Cross-origin resource sharing enabled

## Setup

1. Create database:
```sql
CREATE DATABASE user_management_db;
```

2. Update `application.properties` with your database credentials

3. Run the application:
```bash
./mvnw spring-boot:run
```

## Health Check

```
GET http://localhost:8071/actuator/health
```

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter OAuth2 Authorization Server
- MySQL Connector
- Spring Boot Starter Validation
- Lombok
- Spring Boot Starter Actuator
- JWT Library (jjwt)

## Inter-Service Communication

The User Service provides authentication and authorization for:
- **Product Service**: User validation for product management
- **Order Service**: User verification for order placement
- **Cart Service**: User authentication for cart operations
- **Inventory Service**: Admin user verification for inventory management

## Author

Shamshad Khan - MS in Computer Science Project
