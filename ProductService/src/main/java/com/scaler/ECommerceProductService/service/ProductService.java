package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProductById(Integer id);
}
