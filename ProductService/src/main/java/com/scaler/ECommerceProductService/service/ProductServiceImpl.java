package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.addAll;

@Service("ProductServiceImpl")
public class ProductServiceImpl implements ProductService {

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        return null;
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        ProductRequestDTO product1 = new ProductRequestDTO();
        return null;
    }

    @Override
    public ProductResponseDTO addProduct(ProductResponseDTO product) {
        return null;
    }

    @Override
    public ProductResponseDTO deleteProduct(Integer id) {
        return null;
    }
}
