package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ProductService {
    public ProductResponseDTO getProductById(Integer id) {
        String getProductByIdUrl = "https://fakestoreapi.com/products/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ProductResponseDTO> product = restTemplate.getForEntity(getProductByIdUrl, ProductResponseDTO.class);

        return product.getBody();
    }
}
