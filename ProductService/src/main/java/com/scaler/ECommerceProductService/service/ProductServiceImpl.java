package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service("ProductServiceImpl")
public class ProductServiceImpl implements ProductService {

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        ProductRequestDTO product1 = new ProductRequestDTO();

        List<ProductResponseDTO> products = Arrays.asList();
        return products;
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        return null;
    }
}
