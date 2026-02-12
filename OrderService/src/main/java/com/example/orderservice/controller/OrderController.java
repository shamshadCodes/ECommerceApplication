package com.example.orderservice.controller;

import com.example.orderservice.dto.ApiResponse;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UpdateOrderStatusRequest;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        log.info("REST request to create order for user: {}", request.getUserId());
        
        OrderResponse orderResponse = orderService.createOrder(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", orderResponse));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable String id) {
        log.info("REST request to get order by ID: {}", id);
        
        OrderResponse orderResponse = orderService.getOrderById(id);
        
        return ResponseEntity.ok(ApiResponse.success(orderResponse));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByUserId(
            @PathVariable String userId) {
        log.info("REST request to get orders for user: {}", userId);
        
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        
        return ResponseEntity.ok(ApiResponse.success(orders));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        log.info("REST request to get all orders");
        
        List<OrderResponse> orders = orderService.getAllOrders();
        
        return ResponseEntity.ok(ApiResponse.success(orders));
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        log.info("REST request to update order status for order: {}", id);
        
        OrderResponse orderResponse = orderService.updateOrderStatus(id, request);
        
        return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", orderResponse));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable String id) {
        log.info("REST request to cancel order: {}", id);
        
        orderService.cancelOrder(id);
        
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", null));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(
            @PathVariable OrderStatus status) {
        log.info("REST request to get orders with status: {}", status);
        
        List<OrderResponse> orders = orderService.getOrdersByStatus(status);
        
        return ResponseEntity.ok(ApiResponse.success(orders));
    }
    
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<ApiResponse<Long>> getOrderCountByUserId(@PathVariable String userId) {
        log.info("REST request to get order count for user: {}", userId);
        
        long count = orderService.getOrderCountByUserId(userId);
        
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}

