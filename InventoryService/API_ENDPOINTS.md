# Inventory Service API Documentation

## Base URL
```
http://localhost:8081/api/v1/inventory
```

## Endpoints

### 1. Create Inventory Item
**POST** `/api/v1/inventory`

Creates a new inventory item.

**Request Body:**
```json
{
  "name": "Laptop",
  "description": "Dell XPS 15",
  "category": "Electronics",
  "quantity": 50,
  "price": 1299.99,
  "minimumStockLevel": 10
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "Item created successfully",
  "data": {
    "id": "uuid-here",
    "name": "Laptop",
    "description": "Dell XPS 15",
    "category": "Electronics",
    "quantity": 50,
    "price": 1299.99,
    "minimumStockLevel": 10,
    "createdAt": "2025-10-30T10:00:00",
    "updatedAt": "2025-10-30T10:00:00",
    "lowStock": false,
    "outOfStock": false
  },
  "timestamp": "2025-10-30T10:00:00"
}
```

---

### 2. Get Item by ID
**GET** `/api/v1/inventory/{id}`

Retrieves a specific inventory item by ID.

**Response:** `200 OK`

---

### 3. Get All Items
**GET** `/api/v1/inventory`

Retrieves all inventory items. Supports optional category filtering.

**Query Parameters:**
- `category` (optional): Filter by category

**Examples:**
- Get all items: `GET /api/v1/inventory`
- Filter by category: `GET /api/v1/inventory?category=Electronics`

**Response:** `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid-1",
      "name": "Laptop",
      "category": "Electronics",
      ...
    }
  ],
  "timestamp": "2025-10-30T10:00:00"
}
```

---

### 4. Get Low Stock Items
**GET** `/api/v1/inventory/low-stock`

Retrieves items where quantity is at or below minimum stock level.

**Response:** `200 OK`

---

### 5. Get Out of Stock Items
**GET** `/api/v1/inventory/out-of-stock`

Retrieves items with zero or negative quantity.

**Response:** `200 OK`

---

### 6. Update Item
**PUT** `/api/v1/inventory/{id}`

Updates an existing inventory item. All fields are optional.

**Request Body:**
```json
{
  "name": "Updated Laptop",
  "description": "Dell XPS 15 - Updated",
  "category": "Electronics",
  "quantity": 45,
  "price": 1199.99,
  "minimumStockLevel": 15
}
```

**Response:** `200 OK`

---

### 7. Delete Item
**DELETE** `/api/v1/inventory/{id}`

Deletes an inventory item.

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Item deleted successfully",
  "data": null,
  "timestamp": "2025-10-30T10:00:00"
}
```

---

### 8. Add Stock
**POST** `/api/v1/inventory/{id}/stock/add`

Increases the quantity of an item.

**Request Body:**
```json
{
  "quantity": 20,
  "reason": "Restocking from supplier"
}
```

**Response:** `200 OK`

---

### 9. Reduce Stock
**POST** `/api/v1/inventory/{id}/stock/reduce`

Decreases the quantity of an item. Throws error if insufficient stock.

**Request Body:**
```json
{
  "quantity": 5,
  "reason": "Sold to customer"
}
```

**Response:** `200 OK`

**Error Response (Insufficient Stock):** `400 Bad Request`
```json
{
  "timestamp": "2025-10-30T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Insufficient stock for item uuid-here. Requested: 100, Available: 50",
  "path": "/api/v1/inventory/uuid-here/stock/reduce"
}
```

---

### 10. Check Availability
**GET** `/api/v1/inventory/{id}/availability?quantity={quantity}`

Checks if a specific quantity is available for an item.

**Query Parameters:**
- `quantity` (required): Quantity to check

**Example:** `GET /api/v1/inventory/uuid-here/availability?quantity=10`

**Response:** `200 OK`
```json
{
  "success": true,
  "data": true,
  "timestamp": "2025-10-30T10:00:00"
}
```

---

### 11. Get Total Item Count
**GET** `/api/v1/inventory/stats/count`

Returns the total number of unique items in inventory.

**Response:** `200 OK`
```json
{
  "success": true,
  "data": 42,
  "timestamp": "2025-10-30T10:00:00"
}
```

---

## Error Responses

### Validation Error (400)
```json
{
  "timestamp": "2025-10-30T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/inventory",
  "details": [
    "name: Name is required",
    "quantity: Quantity must be non-negative"
  ]
}
```

### Resource Not Found (404)
```json
{
  "timestamp": "2025-10-30T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "InventoryItem not found with id: 'invalid-uuid'",
  "path": "/api/v1/inventory/invalid-uuid"
}
```

---

## Health Check Endpoints

### Application Health
**GET** `/actuator/health`

Returns the health status of the application.

### Metrics
**GET** `/actuator/metrics`

Returns application metrics.

### Info
**GET** `/actuator/info`

Returns application information.

