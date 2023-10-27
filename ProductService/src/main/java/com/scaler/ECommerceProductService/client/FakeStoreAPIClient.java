package com.scaler.ECommerceProductService.client;

import com.scaler.ECommerceProductService.dto.FakeStoreProductRequestDTO;
import com.scaler.ECommerceProductService.dto.FakeStoreProductResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static com.scaler.ECommerceProductService.utils.ProductUtils.isNull;

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

    public List<FakeStoreProductResponseDTO> getAllProducts() {
        String getAllProductsURL = fakeStoreAPIURL + fakeStoreAPIPathProduct;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO[]> productsResponse = restTemplate.getForEntity(getAllProductsURL, FakeStoreProductResponseDTO[].class);

        return List.of(productsResponse.getBody());
    }

    public FakeStoreProductResponseDTO getProductById(int id){
        String getProductByIdURL = fakeStoreAPIURL + fakeStoreAPIPathProduct + "/" + id;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO> product = restTemplate.getForEntity(getProductByIdURL, FakeStoreProductResponseDTO.class);
        return product.getBody();
    }

    public FakeStoreProductResponseDTO createProduct(FakeStoreProductRequestDTO fakeStoreProductRequestDTO){
        String createProductURL = fakeStoreAPIURL + fakeStoreAPIPathProduct;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO> product = restTemplate.postForEntity(createProductURL, fakeStoreProductRequestDTO, FakeStoreProductResponseDTO.class);
        return product.getBody();
    }

    public FakeStoreProductResponseDTO deleteProduct(int id){
        String deleteProductURL = fakeStoreAPIURL + fakeStoreAPIPathProduct + "/" + id;
        RestTemplate restTemplate = restTemplateBuilder.build();

        try{
            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.DELETE, new URI(deleteProductURL));
            ResponseEntity<FakeStoreProductResponseDTO> deletedProduct = restTemplate.exchange(requestEntity, FakeStoreProductResponseDTO.class);
            return deletedProduct.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /*public FakeStoreProductResponseDTO updateProduct(int id, FakeStoreProductRequestDTO fakeStoreProductRequestDTO){
        String updateProductURL = fakeStoreAPIURL + fakeStoreAPIPathProduct + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        FakeStoreProductResponseDTO updatedProduct = restTemplate.patchForObject(updateProductURL, fakeStoreProductRequestDTO, FakeStoreProductResponseDTO.class);
        return updatedProduct;
    }*/

    public FakeStoreProductResponseDTO updateProduct(int id, FakeStoreProductRequestDTO fakeStoreProductRequestDTO){
        String updateProductURL = fakeStoreAPIURL + fakeStoreAPIPathProduct + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.PUT, new URI(updateProductURL));
            ResponseEntity<FakeStoreProductResponseDTO> updatedProduct = restTemplate.exchange(requestEntity, FakeStoreProductResponseDTO.class);
            return updatedProduct.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public FakeStoreProductResponseDTO modifyProduct(int id, FakeStoreProductRequestDTO fakeStoreProductRequestDTO){
        String updateProductURL = fakeStoreAPIURL + fakeStoreAPIPathProduct + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.PATCH, new URI(updateProductURL));
            ResponseEntity<FakeStoreProductResponseDTO> updatedProduct = restTemplate.exchange(requestEntity, FakeStoreProductResponseDTO.class);
            return updatedProduct.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
