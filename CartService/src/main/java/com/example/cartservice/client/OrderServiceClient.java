package com.example.cartservice.client;

import com.example.cartservice.model.Cart;
import com.example.cartservice.model.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderServiceClient {
    
    private final RestTemplate restTemplate;
    private final String orderServiceUrl;
    
    public OrderServiceClient(RestTemplate restTemplate,
                              @Value("${order.service.url}") String orderServiceUrl) {
        this.restTemplate = restTemplate;
        this.orderServiceUrl = orderServiceUrl;
    }
    
    public String createOrderFromCart(Cart cart) {
        try {
            String url = String.format("%s/api/v1/orders", orderServiceUrl);
            
            log.info("Creating order from cart for user: {}", cart.getUserId());
            
            Map<String, Object> orderRequest = new HashMap<>();
            orderRequest.put("userId", cart.getUserId());
            
            List<Map<String, Object>> items = cart.getCartItems().stream()
                    .map(item -> {
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("productId", item.getProductId());
                        itemMap.put("productName", item.getProductName());
                        itemMap.put("quantity", item.getQuantity());
                        itemMap.put("price", item.getPrice());
                        return itemMap;
                    })
                    .collect(Collectors.toList());
            
            orderRequest.put("items", items);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, orderRequest, Map.class);
            
            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                return (String) data.get("id");
            }
            
            throw new RuntimeException("Failed to create order from cart");
        } catch (Exception e) {
            log.error("Error creating order from cart: ", e);
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }
}

