package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return null;
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        String getProductByIdUrl = "https://fakestoreapi.com/products/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ProductResponseDTO> product = restTemplate.getForEntity(getProductByIdUrl, ProductResponseDTO.class);

        return product.getBody();
    }
}
