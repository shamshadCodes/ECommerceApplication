package com.example.cartservice.exception;

public class CartItemNotFoundException extends RuntimeException {
    
    public CartItemNotFoundException(String itemId) {
        super("Cart item not found with ID: " + itemId);
    }
}
