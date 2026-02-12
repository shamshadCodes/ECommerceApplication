package com.example.orderservice.service;

import com.example.orderservice.client.InventoryServiceClient;
import com.example.orderservice.dto.*;
import com.example.orderservice.exception.InsufficientStockException;
import com.example.orderservice.exception.InvalidOrderException;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;
    
    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for user: {}", request.getUserId());
        
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new InvalidOrderException("Order must contain at least one item");
        }
        
        for (OrderItemRequest item : request.getItems()) {
            boolean available = inventoryServiceClient.checkAvailability(
                    item.getProductId(), item.getQuantity());
            
            if (!available) {
                throw new InsufficientStockException(
                        item.getProductId(), item.getQuantity(), 0);
            }
        }
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (OrderItemRequest itemRequest : request.getItems()) {
            BigDecimal subtotal = itemRequest.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            
            OrderItem orderItem = OrderItem.builder()
                    .productId(itemRequest.getProductId())
                    .productName(itemRequest.getProductName())
                    .quantity(itemRequest.getQuantity())
                    .price(itemRequest.getPrice())
                    .subtotal(subtotal)
                    .build();
            
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(subtotal);
        }
        
        Order order = Order.builder()
                .userId(request.getUserId())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .shippingAddress(request.getShippingAddress())
                .paymentMethod(request.getPaymentMethod())
                .notes(request.getNotes())
                .orderItems(new ArrayList<>())
                .build();
        
        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }
        
        Order savedOrder = orderRepository.save(order);
        
        for (OrderItemRequest item : request.getItems()) {
            boolean reduced = inventoryServiceClient.reduceStock(
                    item.getProductId(), item.getQuantity());
            
            if (!reduced) {
                log.warn("Failed to reduce stock for product: {}", item.getProductId());
            }
        }
        
        log.info("Order created successfully with ID: {}", savedOrder.getId());
        
        return mapToOrderResponse(savedOrder);
    }
    
    @Override
    public OrderResponse getOrderById(String orderId) {
        log.info("Fetching order with ID: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        return mapToOrderResponse(order);
    }
    
    @Override
    public List<OrderResponse> getOrdersByUserId(String userId) {
        log.info("Fetching orders for user: {}", userId);
        
        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
        
        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        
        List<Order> orders = orderRepository.findAll();
        
        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public OrderResponse updateOrderStatus(String orderId, UpdateOrderStatusRequest request) {
        log.info("Updating order status for order: {} to {}", orderId, request.getStatus());
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        order.setStatus(request.getStatus());
        
        if (request.getNotes() != null && !request.getNotes().isEmpty()) {
            order.setNotes(request.getNotes());
        }
        
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Order status updated successfully");
        
        return mapToOrderResponse(updatedOrder);
    }
    
    @Override
    @Transactional
    public void cancelOrder(String orderId) {
        log.info("Cancelling order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderException("Cannot cancel a delivered order");
        }
        
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderException("Order is already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        for (OrderItem item : order.getOrderItems()) {
            boolean restored = inventoryServiceClient.restoreStock(
                    item.getProductId(), item.getQuantity());

            if (!restored) {
                log.warn("Failed to restore stock for product: {}", item.getProductId());
            }
        }

        log.info("Order cancelled successfully");
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        log.info("Fetching orders with status: {}", status);

        List<Order> orders = orderRepository.findByStatus(status);

        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long getOrderCountByUserId(String userId) {
        log.info("Counting orders for user: {}", userId);

        return orderRepository.countByUserId(userId);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(item -> OrderItemDto.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .notes(order.getNotes())
                .items(itemDtos)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}

