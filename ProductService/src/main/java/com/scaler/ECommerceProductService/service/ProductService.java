package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO getProductById(Integer id);
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO addProduct(ProductRequestDTO product);

    boolean deleteProduct(Integer id);
}
