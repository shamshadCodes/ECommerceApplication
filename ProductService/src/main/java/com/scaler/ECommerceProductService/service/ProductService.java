package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {
    ProductListResponseDTO getAllProducts();

    ProductResponseDTO getProductById(Integer id) throws ProductNotFoundException;

    ProductResponseDTO addProduct(ProductRequestDTO product);

    boolean deleteProduct(Integer id);

    ProductResponseDTO updateProduct(Integer id, ProductRequestDTO product);
}
