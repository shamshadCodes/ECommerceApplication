package com.example.cartservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class ProductServiceClient {
    
    private final RestTemplate restTemplate;
    private final String productServiceUrl;
    
    public ProductServiceClient(RestTemplate restTemplate,
                                @Value("${product.service.url}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }
    
    public Map<String, Object> getProductById(String productId) {
        try {
            String url = String.format("%s/products/%s", productServiceUrl, productId);
            
            log.info("Fetching product details for product: {}", productId);
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            
            return null;
        } catch (Exception e) {
            log.error("Error fetching product details: ", e);
            return null;
        }
    }
}

