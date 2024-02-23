package com.scaler.ECommerceProductService.client;

import com.scaler.ECommerceProductService.dto.FakeStoreProductRequestDTO;
import com.scaler.ECommerceProductService.dto.FakeStoreProductResponseDTO;
import com.scaler.ECommerceProductService.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.scaler.ECommerceProductService.mapper.ProductMapper.fakeStoreProductToProduct;

@Component
public class FakeStoreAPIClient {
    private final RestTemplateBuilder restTemplateBuilder;
    private final String fakeStoreAPIURL;
    @Value("${fakeStore.api.path.product}")
    private String fakeStoreAPIPathProduct;

    @Autowired
    public FakeStoreAPIClient (RestTemplateBuilder restTemplateBuilder, @Value("${fakeStore.api.url}") String fakeStoreAPIURL){
        this.restTemplateBuilder = restTemplateBuilder;
        this.fakeStoreAPIURL = fakeStoreAPIURL;
    }

    public List<Product> getAllProducts() {
        String getAllProductsURL = fakeStoreAPIURL + fakeStoreAPIPathProduct;
        RestTemplate restTemplate = restTemplateBuilder.build();
        List<Product> productList = new ArrayList<>();

        ResponseEntity<FakeStoreProductResponseDTO[]> productsResponse = restTemplate.getForEntity(getAllProductsURL, FakeStoreProductResponseDTO[].class);
        if(productsResponse.getBody() == null){
            return productList;
        }

        List<FakeStoreProductResponseDTO> fakeStoreProductList = Arrays.stream(productsResponse.getBody()).toList();
        for(FakeStoreProductResponseDTO fakeStoreProduct: fakeStoreProductList){
            productList.add(fakeStoreProductToProduct(fakeStoreProduct));
        }

        return productList;
    }

    public Product getProductById(int id){
        String getProductByIdURL = fakeStoreAPIURL + fakeStoreAPIPathProduct + "/" + id;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO> product = restTemplate.getForEntity(getProductByIdURL, FakeStoreProductResponseDTO.class);

        return fakeStoreProductToProduct(product.getBody());
    }

    public Product createProduct(FakeStoreProductRequestDTO fakeStoreProductRequestDTO){
        String createProductURL = fakeStoreAPIURL + fakeStoreAPIPathProduct;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductResponseDTO> product = restTemplate.postForEntity(createProductURL, fakeStoreProductRequestDTO, FakeStoreProductResponseDTO.class);
        return fakeStoreProductToProduct(product.getBody());
    }

    public Product deleteProduct(int id){
        String deleteProductURL = fakeStoreAPIURL + fakeStoreAPIPathProduct + "/" + id;
        RestTemplate restTemplate = restTemplateBuilder.build();

        try{
            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.DELETE, new URI(deleteProductURL));
            ResponseEntity<FakeStoreProductResponseDTO> deletedProduct = restTemplate.exchange(requestEntity, FakeStoreProductResponseDTO.class);
            return fakeStoreProductToProduct(deletedProduct.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public Product updateProduct(int id, FakeStoreProductRequestDTO fakeStoreProductRequestDTO){
        String updateProductURL = fakeStoreAPIURL + fakeStoreAPIPathProduct + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.PUT, new URI(updateProductURL));
            ResponseEntity<FakeStoreProductResponseDTO> updatedProduct = restTemplate.exchange(requestEntity, FakeStoreProductResponseDTO.class);
            return fakeStoreProductToProduct(updatedProduct.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public Product modifyProduct(int id, FakeStoreProductRequestDTO fakeStoreProductRequestDTO){
        String updateProductURL = fakeStoreAPIURL + fakeStoreAPIPathProduct + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.PATCH, new URI(updateProductURL));
            ResponseEntity<FakeStoreProductResponseDTO> updatedProduct = restTemplate.exchange(requestEntity, FakeStoreProductResponseDTO.class);
            return fakeStoreProductToProduct(updatedProduct.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
