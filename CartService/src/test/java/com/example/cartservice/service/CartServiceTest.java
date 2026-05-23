package com.example.cartservice.service;

import com.example.cartservice.client.InventoryServiceClient;
import com.example.cartservice.client.OrderServiceClient;
import com.example.cartservice.dto.AddToCartRequest;
import com.example.cartservice.dto.CartResponse;
import com.example.cartservice.dto.UpdateCartItemRequest;
import com.example.cartservice.exception.CartNotFoundException;
import com.example.cartservice.exception.CartItemNotFoundException;
import com.example.cartservice.model.Cart;
import com.example.cartservice.model.CartItem;
import com.example.cartservice.repository.CartItemRepository;
import com.example.cartservice.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private InventoryServiceClient inventoryServiceClient;

    @Mock
    private OrderServiceClient orderServiceClient;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart testCart;
    private CartItem testCartItem;
    private AddToCartRequest addToCartRequest;

    @BeforeEach
    void setUp() {
        testCart = Cart.builder()
            .userId("user-123")
            .cartItems(new ArrayList<>())
            .build();
        testCart.setId("cart-123");

        testCartItem = CartItem.builder()
            .cart(testCart)
            .productId("product-123")
            .productName("Test Product")
            .quantity(2)
            .price(BigDecimal.valueOf(99.99))
            .subtotal(BigDecimal.valueOf(199.98))
            .build();
        testCartItem.setId("item-123");

        addToCartRequest = AddToCartRequest.builder()
            .userId("user-123")
            .productId("product-123")
            .productName("Test Product")
            .quantity(2)
            .price(BigDecimal.valueOf(99.99))
            .build();
    }

    @Test
    void createCart_WithNewUser_ShouldCreateCart() {
        // Arrange
        String userId = "user-123";
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        CartResponse result = cartService.createCart(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void createCart_WithExistingUser_ShouldReturnExistingCart() {
        // Arrange
        String userId = "user-123";
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(testCart));

        // Act
        CartResponse result = cartService.createCart(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void getCartByUserId_WithValidUser_ShouldReturnCart() {
        // Arrange
        String userId = "user-123";
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(testCart));

        // Act
        CartResponse result = cartService.getCartByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getCartByUserId_WithInvalidUser_ShouldThrowException() {
        // Arrange
        String userId = "invalid-user";
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CartNotFoundException.class, () ->
            cartService.getCartByUserId(userId)
        );
        verify(cartRepository, times(1)).findByUserId(userId);
    }

    @Test
    void addItemToCart_WithAvailableProduct_ShouldAddItem() {
        // Arrange
        when(cartRepository.findByUserId(addToCartRequest.getUserId()))
            .thenReturn(Optional.of(testCart));
        when(inventoryServiceClient.checkAvailability(anyString(), anyInt())).thenReturn(true);
        when(cartItemRepository.findByCartIdAndProductId(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);
        when(cartRepository.findById(anyString())).thenReturn(Optional.of(testCart));

        // Act
        CartResponse result = cartService.addItemToCart(addToCartRequest);

        // Assert
        assertNotNull(result);
        verify(inventoryServiceClient, times(1)).checkAvailability(anyString(), anyInt());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void addItemToCart_WithUnavailableProduct_ShouldThrowException() {
        // Arrange
        when(cartRepository.findByUserId(addToCartRequest.getUserId()))
            .thenReturn(Optional.of(testCart));
        when(inventoryServiceClient.checkAvailability(anyString(), anyInt())).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            cartService.addItemToCart(addToCartRequest)
        );
        verify(inventoryServiceClient, times(1)).checkAvailability(anyString(), anyInt());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void updateCartItem_WithValidItem_ShouldUpdateQuantity() {
        // Arrange
        String itemId = "item-123";
        UpdateCartItemRequest updateRequest = UpdateCartItemRequest.builder()
            .quantity(5)
            .build();

        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        // Act
        CartResponse result = cartService.updateCartItem(itemId, updateRequest);

        // Assert
        assertNotNull(result);
        verify(cartItemRepository, times(1)).findById(itemId);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void updateCartItem_WithInvalidItem_ShouldThrowException() {
        // Arrange
        String itemId = "invalid-item";
        UpdateCartItemRequest updateRequest = UpdateCartItemRequest.builder()
            .quantity(5)
            .build();

        when(cartItemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CartItemNotFoundException.class, () ->
            cartService.updateCartItem(itemId, updateRequest)
        );
        verify(cartItemRepository, times(1)).findById(itemId);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void removeCartItem_WithValidItem_ShouldRemoveItem() {
        // Arrange
        String itemId = "item-123";
        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(testCartItem));
        doNothing().when(cartItemRepository).delete(any(CartItem.class));

        // Act
        CartResponse result = cartService.removeCartItem(itemId);

        // Assert
        assertNotNull(result);
        verify(cartItemRepository, times(1)).findById(itemId);
        verify(cartItemRepository, times(1)).delete(testCartItem);
    }

    @Test
    void clearCart_WithValidCart_ShouldClearAllItems() {
        // Arrange
        String cartId = "cart-123";
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(testCart));
        doNothing().when(cartItemRepository).deleteByCartId(cartId);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        cartService.clearCart(cartId);

        // Assert
        verify(cartRepository, times(1)).findById(cartId);
        verify(cartItemRepository, times(1)).deleteByCartId(cartId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void checkout_WithValidCart_ShouldCreateOrderAndClearCart() {
        // Arrange
        String userId = "user-123";
        testCart.getCartItems().add(testCartItem);
        String orderId = "order-123";

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(testCart));
        when(orderServiceClient.createOrderFromCart(any(Cart.class))).thenReturn(orderId);
        when(cartRepository.findById(anyString())).thenReturn(Optional.of(testCart));
        doNothing().when(cartItemRepository).deleteByCartId(anyString());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        String result = cartService.checkout(userId);

        // Assert
        assertEquals(orderId, result);
        verify(orderServiceClient, times(1)).createOrderFromCart(any(Cart.class));
        verify(cartItemRepository, times(1)).deleteByCartId(anyString());
    }

    @Test
    void checkout_WithEmptyCart_ShouldThrowException() {
        // Arrange
        String userId = "user-123";
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(testCart));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            cartService.checkout(userId)
        );
        verify(orderServiceClient, never()).createOrderFromCart(any(Cart.class));
    }
}
