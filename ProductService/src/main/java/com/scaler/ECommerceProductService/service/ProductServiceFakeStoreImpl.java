package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service("ProductServiceFakeStoreImpl")
public class ProductServiceFakeStoreImpl implements ProductService {
    RestTemplate restTemplate = new RestTemplate();
    @Override
    public List<ProductResponseDTO> getAllProducts() {
        String getAllProductsUrl = "https://fakestoreapi.com/products";
        ResponseEntity<List> products = restTemplate.getForEntity(getAllProductsUrl, List.class);
        return products.getBody();
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) {

        String getProductByIdUrl = "https://fakestoreapi.com/products/" + id;
        ResponseEntity<ProductResponseDTO> product = restTemplate.getForEntity(getProductByIdUrl, ProductResponseDTO.class);

        return product.getBody();
    }
}
