package com.scaler.ECommerceProductService.exception;

public class ProductServiceException extends RuntimeException {
    public ProductServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
