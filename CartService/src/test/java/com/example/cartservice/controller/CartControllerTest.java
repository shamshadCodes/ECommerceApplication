package com.example.cartservice.controller;

import com.example.cartservice.dto.AddToCartRequest;
import com.example.cartservice.dto.ApiResponse;
import com.example.cartservice.dto.CartResponse;
import com.example.cartservice.dto.UpdateCartItemRequest;
import com.example.cartservice.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private CartResponse cartResponse;

    @BeforeEach
    void setUp() {
        cartResponse = CartResponse.builder()
                .id("cart-123")
                .userId("user-123")
                .items(Collections.emptyList())
                .totalAmount(BigDecimal.ZERO)
                .build();
    }

    @Test
    void createCart_ReturnsCreatedCart() {
        when(cartService.createCart("user-123")).thenReturn(cartResponse);

        ResponseEntity<ApiResponse<CartResponse>> response =
                cartController.createCart("user-123");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("cart-123", response.getBody().getData().getId());
        verify(cartService, times(1)).createCart("user-123");
    }

    @Test
    void getCartByUserId_ReturnsCart() {
        when(cartService.getCartByUserId("user-123")).thenReturn(cartResponse);

        ResponseEntity<ApiResponse<CartResponse>> response =
                cartController.getCartByUserId("user-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("cart-123", response.getBody().getData().getId());
        verify(cartService, times(1)).getCartByUserId("user-123");
    }

    @Test
    void addItemToCart_DelegatesToServiceAndReturnsUpdatedCart() {
        AddToCartRequest request = AddToCartRequest.builder()
                .userId("user-123")
                .productId("prod-1")
                .quantity(2)
                .build();

        when(cartService.addItemToCart(request)).thenReturn(cartResponse);

        ResponseEntity<ApiResponse<CartResponse>> response =
                cartController.addItemToCart(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).addItemToCart(request);
    }

    @Test
    void updateCartItem_DelegatesToService() {
        UpdateCartItemRequest request = UpdateCartItemRequest.builder()
                .quantity(3)
                .build();

        when(cartService.updateCartItem(eq("item-1"), any(UpdateCartItemRequest.class)))
                .thenReturn(cartResponse);

        ResponseEntity<ApiResponse<CartResponse>> response =
                cartController.updateCartItem("item-1", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).updateCartItem("item-1", request);
    }

    @Test
    void removeCartItem_DelegatesToService() {
        when(cartService.removeCartItem("item-1")).thenReturn(cartResponse);

        ResponseEntity<ApiResponse<CartResponse>> response =
                cartController.removeCartItem("item-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).removeCartItem("item-1");
    }

    @Test
    void checkout_ReturnsOrderId() {
        when(cartService.checkout("user-123")).thenReturn("order-123");

        ResponseEntity<ApiResponse<String>> response =
                cartController.checkout("user-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("order-123", response.getBody().getData());
        verify(cartService, times(1)).checkout("user-123");
    }

    @Test
    void clearCart_DelegatesToService() {
        doNothing().when(cartService).clearCart("cart-123");

        ResponseEntity<ApiResponse<Void>> response =
                cartController.clearCart("cart-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).clearCart("cart-123");
    }
}
