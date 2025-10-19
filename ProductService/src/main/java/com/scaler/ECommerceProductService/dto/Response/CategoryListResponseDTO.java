package com.scaler.ECommerceProductService.dto.Response;

import lombok.Getter;

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