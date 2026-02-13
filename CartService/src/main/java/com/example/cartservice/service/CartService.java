package com.example.cartservice.service;

import com.example.cartservice.dto.AddToCartRequest;
import com.example.cartservice.dto.CartResponse;
import com.example.cartservice.dto.UpdateCartItemRequest;

public interface CartService {
    
    CartResponse createCart(String userId);
    
    CartResponse getCartByUserId(String userId);
    
    CartResponse addItemToCart(AddToCartRequest request);
    
    CartResponse updateCartItem(String itemId, UpdateCartItemRequest request);
    
    CartResponse removeCartItem(String itemId);
    
    void clearCart(String cartId);
    
    String checkout(String userId);
}

