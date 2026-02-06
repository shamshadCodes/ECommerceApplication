package com.example.inventoryservice.exception;

public class InsufficientStockException extends RuntimeException {
    
    public InsufficientStockException(String message) {
        super(message);
    }
    
    public InsufficientStockException(String itemId, int requested, int available) {
        super(String.format("Insufficient stock for item %s. Requested: %d, Available: %d", 
                itemId, requested, available));
    }
}

