package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductListResponseDTO getAllProducts();

    ProductResponseDTO getProductById(Integer id);

    ProductResponseDTO addProduct(ProductRequestDTO product);

    boolean deleteProduct(Integer id);
}
