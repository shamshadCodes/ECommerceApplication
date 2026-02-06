package com.example.inventoryservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInventoryItemRequest {
    
    private String name;
    private String description;
    private String category;
    
    @Min(value = 0, message = "Quantity must be non-negative")
    private Integer quantity;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private BigDecimal price;
    
    @Min(value = 0, message = "Minimum stock level must be non-negative")
    private Integer minimumStockLevel;
}

