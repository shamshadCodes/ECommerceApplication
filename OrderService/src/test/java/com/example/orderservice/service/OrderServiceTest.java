package com.example.orderservice.service;

import com.example.orderservice.client.InventoryServiceClient;
import com.example.orderservice.event.OrderEventPublisher;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderItemRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UpdateOrderStatusRequest;
import com.example.orderservice.exception.InvalidOrderException;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryServiceClient inventoryServiceClient;

	    @Mock
	    private OrderEventPublisher orderEventPublisher;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;
    private CreateOrderRequest createOrderRequest;
    private OrderItemRequest orderItemRequest;

    @BeforeEach
    void setUp() {
        testOrder = Order.builder()
            .userId("user-123")
            .orderDate(LocalDateTime.now())
            .status(OrderStatus.PENDING)
            .totalAmount(BigDecimal.valueOf(199.98))
            .shippingAddress("123 Test St")
            .paymentMethod("Credit Card")
            .orderItems(new ArrayList<>())
            .build();
        testOrder.setId("order-123");

        orderItemRequest = OrderItemRequest.builder()
            .productId("product-123")
            .productName("Test Product")
            .quantity(2)
            .price(BigDecimal.valueOf(99.99))
            .build();

        createOrderRequest = CreateOrderRequest.builder()
            .userId("user-123")
            .shippingAddress("123 Test St")
            .paymentMethod("Credit Card")
            .items(Arrays.asList(orderItemRequest))
            .build();
    }

    @Test
    void createOrder_WithValidRequest_ShouldCreateOrder() {
        // Arrange
        when(inventoryServiceClient.checkAvailability(anyString(), anyInt())).thenReturn(true);
        when(inventoryServiceClient.reduceStock(anyString(), anyInt())).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
	        OrderResponse result = orderService.createOrder(createOrderRequest);
	        
        // Assert
        assertNotNull(result);
        assertEquals(testOrder.getUserId(), result.getUserId());
        verify(inventoryServiceClient, times(1)).checkAvailability(anyString(), anyInt());
        verify(inventoryServiceClient, times(1)).reduceStock(anyString(), anyInt());
        verify(orderRepository, times(1)).save(any(Order.class));
	        verify(orderEventPublisher, times(1)).publishOrderCreated(any(Order.class));
    }

    @Test
    void createOrder_WithEmptyItems_ShouldThrowException() {
        // Arrange
        CreateOrderRequest emptyRequest = CreateOrderRequest.builder()
            .userId("user-123")
            .shippingAddress("123 Test St")
            .items(new ArrayList<>())
            .build();

        // Act & Assert
        assertThrows(InvalidOrderException.class, () ->
            orderService.createOrder(emptyRequest)
        );
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getOrderById_WithValidId_ShouldReturnOrder() {
        // Arrange
        String orderId = "order-123";
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act
        OrderResponse result = orderService.getOrderById(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void getOrderById_WithInvalidId_ShouldThrowException() {
        // Arrange
        String orderId = "invalid-order";
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () ->
            orderService.getOrderById(orderId)
        );
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void getOrdersByUserId_WithValidUser_ShouldReturnOrders() {
        // Arrange
        String userId = "user-123";
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByUserIdOrderByOrderDateDesc(userId)).thenReturn(orders);

        // Act
        List<OrderResponse> result = orderService.getOrdersByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByUserIdOrderByOrderDateDesc(userId);
    }

    @Test
    void updateOrderStatus_WithValidId_ShouldUpdateStatus() {
        // Arrange
        String orderId = "order-123";
        UpdateOrderStatusRequest updateRequest = UpdateOrderStatusRequest.builder()
            .status(OrderStatus.CONFIRMED)
            .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        OrderResponse result = orderService.updateOrderStatus(orderId, updateRequest);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void cancelOrder_WithPendingOrder_ShouldCancelAndRestoreStock() {
        // Arrange
        String orderId = "order-123";
        OrderItem orderItem = OrderItem.builder()
            .productId("product-123")
            .quantity(2)
            .build();
        testOrder.getOrderItems().add(orderItem);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(inventoryServiceClient.restoreStock(anyString(), anyInt())).thenReturn(true);

        // Act
        orderService.cancelOrder(orderId);

        // Assert
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(inventoryServiceClient, times(1)).restoreStock(anyString(), anyInt());
    }

    @Test
    void cancelOrder_WithDeliveredOrder_ShouldThrowException() {
        // Arrange
        String orderId = "order-123";
        testOrder.setStatus(OrderStatus.DELIVERED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act & Assert
        assertThrows(InvalidOrderException.class, () ->
            orderService.cancelOrder(orderId)
        );
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getOrdersByStatus_WithValidStatus_ShouldReturnOrders() {
        // Arrange
        OrderStatus status = OrderStatus.PENDING;
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByStatus(status)).thenReturn(orders);

        // Act
        List<OrderResponse> result = orderService.getOrdersByStatus(status);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByStatus(status);
    }

    @Test
    void getOrderCountByUserId_WithValidUser_ShouldReturnCount() {
        // Arrange
        String userId = "user-123";
        when(orderRepository.countByUserId(userId)).thenReturn(5L);

        // Act
        long result = orderService.getOrderCountByUserId(userId);

        // Assert
        assertEquals(5L, result);
        verify(orderRepository, times(1)).countByUserId(userId);
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        // Arrange
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        List<OrderResponse> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }
}
