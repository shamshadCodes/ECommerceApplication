package com.example.cartservice.service;

import com.example.cartservice.client.InventoryServiceClient;
import com.example.cartservice.client.OrderServiceClient;
import com.example.cartservice.dto.*;
import com.example.cartservice.exception.CartItemNotFoundException;
import com.example.cartservice.exception.CartNotFoundException;
import com.example.cartservice.model.Cart;
import com.example.cartservice.model.CartItem;
import com.example.cartservice.repository.CartItemRepository;
import com.example.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final OrderServiceClient orderServiceClient;
    
    @Override
    @Transactional
    public CartResponse createCart(String userId) {
        log.info("Creating cart for user: {}", userId);
        
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        if (existingCart.isPresent()) {
            log.info("Cart already exists for user: {}", userId);
            return mapToCartResponse(existingCart.get());
        }
        
        Cart cart = Cart.builder()
                .userId(userId)
                .build();
        
        Cart savedCart = cartRepository.save(cart);
        
        log.info("Cart created successfully with ID: {}", savedCart.getId());
        
        return mapToCartResponse(savedCart);
    }
    
    @Override
    public CartResponse getCartByUserId(String userId) {
        log.info("Fetching cart for user: {}", userId);
        
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));
        
        return mapToCartResponse(cart);
    }
    
    @Override
    @Transactional
    public CartResponse addItemToCart(AddToCartRequest request) {
        log.info("Adding item to cart for user: {}", request.getUserId());
        
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(request.getUserId())
                            .build();
                    return cartRepository.save(newCart);
                });
        
        boolean available = inventoryServiceClient.checkAvailability(
                request.getProductId(), request.getQuantity());
        
        if (!available) {
            throw new RuntimeException("Product not available in requested quantity");
        }
        
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(
                cart.getId(), request.getProductId());
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.updateSubtotal();
            cartItemRepository.save(item);
        } else {
            BigDecimal subtotal = request.getPrice()
                    .multiply(BigDecimal.valueOf(request.getQuantity()));
            
            CartItem cartItem = CartItem.builder()
                    .productId(request.getProductId())
                    .productName(request.getProductName())
                    .quantity(request.getQuantity())
                    .price(request.getPrice())
                    .subtotal(subtotal)
                    .build();
            
            cart.addCartItem(cartItem);
            cartRepository.save(cart);
        }
        
        Cart updatedCart = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new CartNotFoundException(request.getUserId()));
        
        log.info("Item added to cart successfully");
        
        return mapToCartResponse(updatedCart);
    }
    
    @Override
    @Transactional
    public CartResponse updateCartItem(String itemId, UpdateCartItemRequest request) {
        log.info("Updating cart item: {}", itemId);
        
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException(itemId));
        
        cartItem.setQuantity(request.getQuantity());
        cartItem.updateSubtotal();
        
        cartItemRepository.save(cartItem);
        
        Cart cart = cartItem.getCart();
        
        log.info("Cart item updated successfully");
        
        return mapToCartResponse(cart);
    }
    
    @Override
    @Transactional
    public CartResponse removeCartItem(String itemId) {
        log.info("Removing cart item: {}", itemId);
        
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException(itemId));
        
        Cart cart = cartItem.getCart();
        cart.removeCartItem(cartItem);
        
        cartItemRepository.delete(cartItem);
        
        log.info("Cart item removed successfully");

        return mapToCartResponse(cart);
    }

    @Override
    @Transactional
    public void clearCart(String cartId) {
        log.info("Clearing cart: {}", cartId);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        cartItemRepository.deleteByCartId(cartId);
        cart.getCartItems().clear();

        cartRepository.save(cart);

        log.info("Cart cleared successfully");
    }

    @Override
    @Transactional
    public String checkout(String userId) {
        log.info("Checking out cart for user: {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout empty cart");
        }

        String orderId = orderServiceClient.createOrderFromCart(cart);

        clearCart(cart.getId());

        log.info("Checkout completed successfully. Order ID: {}", orderId);

        return orderId;
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemDto> itemDtos = cart.getCartItems().stream()
                .map(item -> CartItemDto.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(itemDtos)
                .totalAmount(cart.getTotalAmount())
                .totalItems(cart.getTotalItems())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}
