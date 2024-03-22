package com.scaler.ECommerceProductService.dto;

import lombok.Getter;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * DTO for {@link com.scaler.ECommerceProductService.model.Category}
 */
@Getter
public class CategoryListResponseDTO implements Serializable {
    List<CategoryResponseDTO> categoryResponseDTOList;

    public CategoryListResponseDTO() {
        this.categoryResponseDTOList = new ArrayList<>();
    }
}