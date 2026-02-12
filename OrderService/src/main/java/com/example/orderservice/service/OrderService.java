package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UpdateOrderStatusRequest;
import com.example.orderservice.model.OrderStatus;

import java.util.List;

public interface OrderService {
    
    OrderResponse createOrder(CreateOrderRequest request);
    
    OrderResponse getOrderById(String orderId);
    
    List<OrderResponse> getOrdersByUserId(String userId);
    
    List<OrderResponse> getAllOrders();
    
    OrderResponse updateOrderStatus(String orderId, UpdateOrderStatusRequest request);
    
    void cancelOrder(String orderId);
    
    List<OrderResponse> getOrdersByStatus(OrderStatus status);
    
    long getOrderCountByUserId(String userId);
}

