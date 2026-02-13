package com.example.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    
    private String id;
    private String userId;
    private List<CartItemDto> items;
    private BigDecimal totalAmount;
    private int totalItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

