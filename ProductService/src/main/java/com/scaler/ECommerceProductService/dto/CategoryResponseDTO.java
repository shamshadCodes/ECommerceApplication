package com.scaler.ECommerceProductService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.scaler.ECommerceProductService.model.Category}
 */
@Getter
@Setter
public class CategoryResponseDTO implements Serializable {
    @NotNull
    UUID id;
    String categoryName;
}