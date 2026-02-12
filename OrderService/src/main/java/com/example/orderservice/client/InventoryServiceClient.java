package com.example.orderservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InventoryServiceClient {
    
    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl;
    
    public InventoryServiceClient(RestTemplate restTemplate,
                                   @Value("${inventory.service.url}") String inventoryServiceUrl) {
        this.restTemplate = restTemplate;
        this.inventoryServiceUrl = inventoryServiceUrl;
    }
    
    public boolean checkAvailability(String productId, int quantity) {
        try {
            String url = String.format("%s/api/v1/inventory/%s/availability?quantity=%d", 
                    inventoryServiceUrl, productId, quantity);
            
            log.info("Checking inventory availability for product: {}, quantity: {}", productId, quantity);
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Object data = body.get("data");
                return data != null && (Boolean) data;
            }
            
            return false;
        } catch (Exception e) {
            log.error("Error checking inventory availability: ", e);
            return false;
        }
    }
    
    public boolean reduceStock(String productId, int quantity) {
        try {
            String url = String.format("%s/api/v1/inventory/%s/stock/reduce", 
                    inventoryServiceUrl, productId);
            
            log.info("Reducing stock for product: {}, quantity: {}", productId, quantity);
            
            Map<String, Integer> request = new HashMap<>();
            request.put("quantity", quantity);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.error("Error reducing stock: ", e);
            return false;
        }
    }
    
    public boolean restoreStock(String productId, int quantity) {
        try {
            String url = String.format("%s/api/v1/inventory/%s/stock/add", 
                    inventoryServiceUrl, productId);
            
            log.info("Restoring stock for product: {}, quantity: {}", productId, quantity);
            
            Map<String, Integer> request = new HashMap<>();
            request.put("quantity", quantity);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.error("Error restoring stock: ", e);
            return false;
        }
    }
}

