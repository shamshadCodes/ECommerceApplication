package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    ProductResponseDTO getProductById(Integer id);
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO addProduct(ProductResponseDTO product);

    ProductResponseDTO deleteProduct(Integer id);
}
