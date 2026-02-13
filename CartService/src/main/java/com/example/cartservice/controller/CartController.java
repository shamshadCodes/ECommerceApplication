package com.example.cartservice.controller;

import com.example.cartservice.dto.AddToCartRequest;
import com.example.cartservice.dto.ApiResponse;
import com.example.cartservice.dto.CartResponse;
import com.example.cartservice.dto.UpdateCartItemRequest;
import com.example.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<CartResponse>> createCart(@RequestParam String userId) {
        log.info("REST request to create cart for user: {}", userId);
        
        CartResponse cartResponse = cartService.createCart(userId);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cart created successfully", cartResponse));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<CartResponse>> getCartByUserId(@PathVariable String userId) {
        log.info("REST request to get cart for user: {}", userId);
        
        CartResponse cartResponse = cartService.getCartByUserId(userId);
        
        return ResponseEntity.ok(ApiResponse.success(cartResponse));
    }
    
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(
            @Valid @RequestBody AddToCartRequest request) {
        log.info("REST request to add item to cart for user: {}", request.getUserId());
        
        CartResponse cartResponse = cartService.addItemToCart(request);
        
        return ResponseEntity.ok(ApiResponse.success("Item added to cart successfully", cartResponse));
    }
    
    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
            @PathVariable String itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        log.info("REST request to update cart item: {}", itemId);
        
        CartResponse cartResponse = cartService.updateCartItem(itemId, request);
        
        return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully", cartResponse));
    }
    
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeCartItem(@PathVariable String itemId) {
        log.info("REST request to remove cart item: {}", itemId);
        
        CartResponse cartResponse = cartService.removeCartItem(itemId);
        
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully", cartResponse));
    }
    
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<String>> checkout(@RequestParam String userId) {
        log.info("REST request to checkout cart for user: {}", userId);
        
        String orderId = cartService.checkout(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Checkout completed successfully", orderId));
    }
    
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable String cartId) {
        log.info("REST request to clear cart: {}", cartId);
        
        cartService.clearCart(cartId);
        
        return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully", null));
    }
}

