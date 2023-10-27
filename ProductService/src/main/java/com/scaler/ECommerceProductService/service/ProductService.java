package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;

public interface ProductService {
    ProductListResponseDTO getAllProducts();

    ProductResponseDTO getProductById(Integer id) throws ProductNotFoundException;

    ProductResponseDTO addProduct(ProductRequestDTO product);

    ProductResponseDTO deleteProduct(Integer id) throws ProductNotFoundException;

    ProductResponseDTO updateProduct(Integer id, ProductRequestDTO product);
}
