package com.scaler.ECommerceProductService.mapper;

import com.scaler.ECommerceProductService.dto.CategoryListResponseDTO;
import com.scaler.ECommerceProductService.dto.CategoryResponseDTO;
import com.scaler.ECommerceProductService.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
    public static CategoryListResponseDTO categoryListToCategoryListResponseDTO(List<Category> categoryList){
        CategoryListResponseDTO categoryListResponseDTO = new CategoryListResponseDTO();

        for(Category category: categoryList){
            CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
            categoryResponseDTO.setId(category.getId());
            categoryResponseDTO.setCategoryName(category.getCategoryName());
            categoryListResponseDTO.getCategoryResponseDTOList().add(categoryResponseDTO);
        }
        return categoryListResponseDTO;
    }
}
