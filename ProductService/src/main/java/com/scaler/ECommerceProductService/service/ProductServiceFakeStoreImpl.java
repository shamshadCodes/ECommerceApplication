package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service("ProductServiceFakeStoreImpl")
public class ProductServiceFakeStoreImpl implements ProductService {
    private RestTemplateBuilder restTemplateBuilder;

    public ProductServiceFakeStoreImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        String getAllProductsUrl = "https://fakestoreapi.com/products";
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<List> products = restTemplate.getForEntity(getAllProductsUrl, List.class);

        return products.getBody();
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        String getProductByIdUrl = "https://fakestoreapi.com/products/" + id;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<ProductResponseDTO> product = restTemplate.getForEntity(getProductByIdUrl, ProductResponseDTO.class);

        return product.getBody();
    }

    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String getProductByIdUrl = "https://fakestoreapi.com/products/";
        ResponseEntity<ProductResponseDTO> productResponse = restTemplate.postForEntity(getProductByIdUrl, product, ProductResponseDTO.class);
        return productResponse.getBody();
    }

    @Override
    public boolean deleteProduct(Integer id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String getProductByIdUrl = "https://fakestoreapi.com/products/" + id;
        restTemplate.delete(getProductByIdUrl);
        return true;
    }
}
