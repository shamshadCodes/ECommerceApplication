package com.scaler.ECommerceProductService.client;

import com.scaler.ECommerceProductService.dto.FakeStoreProductResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FakeStoreAPIClient {
    private RestTemplateBuilder restTemplateBuilder;
    private String fakeStoreAPIURL;
    @Value("${fakeStore.api.path.product}")
    private String fakeStoreAPIPathProduct;

    public FakeStoreAPIClient (RestTemplateBuilder restTemplateBuilder, @Value("${fakeStore.api.url}") String fakeStoreAPIURL){
        this.restTemplateBuilder = restTemplateBuilder;
        this.fakeStoreAPIURL = fakeStoreAPIURL;
    }

    public List<FakeStoreProductResponseDTO> getAllProducts(){
        String getAllProductsURL = fakeStoreAPIURL+fakeStoreAPIPathProduct;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO[]> productsResponse = restTemplate.getForEntity(getAllProductsURL, FakeStoreProductResponseDTO[].class);
        return List.of(productsResponse.getBody());
    }
}
