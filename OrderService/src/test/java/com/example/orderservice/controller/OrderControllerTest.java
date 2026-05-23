package com.example.orderservice.controller;

import com.example.orderservice.dto.ApiResponse;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderItemRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UpdateOrderStatusRequest;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        orderResponse = OrderResponse.builder()
                .id("order-123")
                .userId("user-123")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(199.99))
                .shippingAddress("123 Test St")
                .paymentMethod("Credit Card")
                .items(Collections.emptyList())
                .build();
    }

    @Test
    void createOrder_ReturnsCreatedOrder() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId("user-123");
        request.setItems(List.of(OrderItemRequest.builder()
                .productId("prod-1")
                .quantity(2)
                .build()));

        when(orderService.createOrder(request)).thenReturn(orderResponse);

        ResponseEntity<ApiResponse<OrderResponse>> response =
                orderController.createOrder(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("order-123", response.getBody().getData().getId());
        verify(orderService, times(1)).createOrder(request);
    }

    @Test
    void getOrderById_ReturnsOrder() {
        when(orderService.getOrderById("order-123")).thenReturn(orderResponse);

        ResponseEntity<ApiResponse<OrderResponse>> response =
                orderController.getOrderById("order-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("order-123", response.getBody().getData().getId());
        verify(orderService, times(1)).getOrderById("order-123");
    }

    @Test
    void getOrdersByUserId_ReturnsList() {
        when(orderService.getOrdersByUserId("user-123"))
                .thenReturn(List.of(orderResponse));

        ResponseEntity<ApiResponse<List<OrderResponse>>> response =
                orderController.getOrdersByUserId("user-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
        verify(orderService, times(1)).getOrdersByUserId("user-123");
    }

    @Test
    void getAllOrders_ReturnsList() {
        when(orderService.getAllOrders()).thenReturn(List.of(orderResponse));

        ResponseEntity<ApiResponse<List<OrderResponse>>> response =
                orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void updateOrderStatus_DelegatesToService() {
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setStatus(OrderStatus.CONFIRMED);

        when(orderService.updateOrderStatus(eq("order-123"), any(UpdateOrderStatusRequest.class)))
                .thenReturn(orderResponse);

        ResponseEntity<ApiResponse<OrderResponse>> response =
                orderController.updateOrderStatus("order-123", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderService, times(1))
                .updateOrderStatus("order-123", request);
    }

    @Test
    void cancelOrder_DelegatesToService() {
        doNothing().when(orderService).cancelOrder("order-123");

        ResponseEntity<ApiResponse<Void>> response =
                orderController.cancelOrder("order-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderService, times(1)).cancelOrder("order-123");
    }

    @Test
    void getOrdersByStatus_ReturnsList() {
        when(orderService.getOrdersByStatus(OrderStatus.PENDING))
                .thenReturn(List.of(orderResponse));

        ResponseEntity<ApiResponse<List<OrderResponse>>> response =
                orderController.getOrdersByStatus(OrderStatus.PENDING);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
        verify(orderService, times(1)).getOrdersByStatus(OrderStatus.PENDING);
    }

    @Test
    void getOrderCountByUserId_ReturnsCount() {
        when(orderService.getOrderCountByUserId("user-123")).thenReturn(5L);

        ResponseEntity<ApiResponse<Long>> response =
                orderController.getOrderCountByUserId("user-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody().getData());
        verify(orderService, times(1)).getOrderCountByUserId("user-123");
    }
}
