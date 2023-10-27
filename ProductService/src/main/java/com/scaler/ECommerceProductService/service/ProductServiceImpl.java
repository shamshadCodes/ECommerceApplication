package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import org.springframework.stereotype.Service;

@Service("ProductServiceImpl")
public class ProductServiceImpl implements ProductService {

    @Override
    public ProductListResponseDTO getAllProducts() {
        ProductRequestDTO product1 = new ProductRequestDTO();
        return null;
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        return null;
    }

    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO product) {
        return null;
    }

    @Override
    public ProductResponseDTO deleteProduct(Integer id) {
        return null;
    }

    @Override
    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO product) {
        return null;
    }

    @Override
    public ProductResponseDTO modifyProduct(Integer id, ProductRequestDTO product) {
        return null;
    }
}
