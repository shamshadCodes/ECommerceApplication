package com.scaler.ECommerceProductService.mapper;

import com.scaler.ECommerceProductService.dto.FakeStoreProductResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;

public class ProductMapper {
    public static ProductResponseDTO fakeStoreProductResponseToProductResponse(FakeStoreProductResponseDTO fakeStoreProductResponseDTO){
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(fakeStoreProductResponseDTO.getId());
        productResponseDTO.setTitle(fakeStoreProductResponseDTO.getTitle());
        productResponseDTO.setPrice(fakeStoreProductResponseDTO.getPrice());
        productResponseDTO.setCategory(fakeStoreProductResponseDTO.getCategory());
        productResponseDTO.setImage(fakeStoreProductResponseDTO.getImage());
        productResponseDTO.setDescription(fakeStoreProductResponseDTO.getDescription());

        return productResponseDTO;
    }
}
