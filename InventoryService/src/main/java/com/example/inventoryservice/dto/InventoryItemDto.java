package com.example.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDto {
    
    private String id;
    private String name;
    private String description;
    private String category;
    private Integer quantity;
    private BigDecimal price;
    private Integer minimumStockLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean lowStock;
    private boolean outOfStock;
}
