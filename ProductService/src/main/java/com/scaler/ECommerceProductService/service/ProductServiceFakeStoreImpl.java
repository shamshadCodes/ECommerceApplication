package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.client.FakeStoreAPIClient;
import com.scaler.ECommerceProductService.dto.FakeStoreProductResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.scaler.ECommerceProductService.mapper.ProductMapper.fakeStoreProductResponseToProductResponse;

@Service("ProductServiceFakeStoreImpl")
public class ProductServiceFakeStoreImpl implements ProductService {
    private FakeStoreAPIClient fakeStoreAPIClient;
    private RestTemplateBuilder restTemplateBuilder;

    public ProductServiceFakeStoreImpl(FakeStoreAPIClient fakeStoreAPIClient, RestTemplateBuilder restTemplateBuilder) {
        this.fakeStoreAPIClient = fakeStoreAPIClient;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public ProductListResponseDTO getAllProducts() {
        List<FakeStoreProductResponseDTO> products = fakeStoreAPIClient.getAllProducts();
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        for(FakeStoreProductResponseDTO fakeStoreProduct: products){
            productListResponseDTO.getProductList().add(fakeStoreProductResponseToProductResponse(fakeStoreProduct));
        }

        return productListResponseDTO;
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

    @Override
    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String getProductByIdUrl = "https://fakestoreapi.com/products/" + id;
        ProductResponseDTO updatedProduct = restTemplate.patchForObject(getProductByIdUrl, product, ProductResponseDTO.class);
        return updatedProduct;
    }
}
