package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.client.FakeStoreAPIClient;
import com.scaler.ECommerceProductService.dto.*;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;
import com.scaler.ECommerceProductService.exception.ProductServiceException;
import com.scaler.ECommerceProductService.model.Category;
import com.scaler.ECommerceProductService.model.Product;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.scaler.ECommerceProductService.mapper.ProductMapper.fakeStoreProductToProduct;
import static com.scaler.ECommerceProductService.mapper.ProductMapper.productRequestToFakeStoreProductRequest;
import static com.scaler.ECommerceProductService.utils.ProductUtils.isNull;

@Service("ProductServiceFakeStoreImpl")
public class ProductServiceFakeStoreImpl implements ProductService {
    private final FakeStoreAPIClient fakeStoreAPIClient;
    private final RedisTemplate<String, FakeStoreProductResponseDTO> redisTemplate;

    public ProductServiceFakeStoreImpl(FakeStoreAPIClient fakeStoreAPIClient, RedisTemplate redisTemplate) {
        this.fakeStoreAPIClient = fakeStoreAPIClient;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Product> getAllProducts() {
        List<FakeStoreProductResponseDTO> fakeStoreProductList = fakeStoreAPIClient.getAllProducts();
        List<Product> productList = new ArrayList<>();

        for(FakeStoreProductResponseDTO product: fakeStoreProductList){
            productList.add(fakeStoreProductToProduct(product));
        }
        return productList;
    }

    @Override
    public List<Category> getAllCategories() {
        return null;
    }

    @Override
    public List<Product> getProductsByCategory(String category) throws ProductServiceException {
        List<FakeStoreProductResponseDTO> fakeStoreProductList = fakeStoreAPIClient.getAllProductsByCategory(category);
        List<Product> productList = new ArrayList<>();

        for(FakeStoreProductResponseDTO fakeStoreProduct: fakeStoreProductList){
            productList.add(fakeStoreProductToProduct(fakeStoreProduct));
        }

        return productList;
    }

    @Override
    public Product getProductById(String id) throws ProductNotFoundException {
        FakeStoreProductResponseDTO redisResponse = (FakeStoreProductResponseDTO) redisTemplate.opsForHash().get("PRODUCTS", id);

        if(!isNull(redisResponse)){
            return fakeStoreProductToProduct(redisResponse);
        }

        FakeStoreProductResponseDTO fakeStoreProduct = fakeStoreAPIClient.getProductById(Integer.parseInt(id));
        if(isNull(fakeStoreProduct)){
            throw new ProductNotFoundException("Product not found with id: " + id);
        }

        redisTemplate.opsForHash().put("PRODUCTS", id, fakeStoreProduct);
        return fakeStoreProductToProduct(fakeStoreProduct);
    }

    @Override
    public Product addProduct(ProductRequestDTO productRequestDTO) {
        FakeStoreProductRequestDTO requestDTO = productRequestToFakeStoreProductRequest(productRequestDTO);
        FakeStoreProductResponseDTO createdProduct = fakeStoreAPIClient.createProduct(requestDTO);

        redisTemplate.opsForHash().put("PRODUCTS", createdProduct.getId(), createdProduct);

        return fakeStoreProductToProduct(createdProduct);
    }

    @Override
    public Product deleteProduct(String id) throws ProductNotFoundException {
        FakeStoreProductResponseDTO fakeStoreProduct = fakeStoreAPIClient.deleteProduct(Integer.parseInt(id));
        if(isNull(fakeStoreProduct)){
            throw new ProductNotFoundException("Product to be deleted not found!!!");
        }
        redisTemplate.delete(id);
        return fakeStoreProductToProduct(fakeStoreProduct);
    }

    @Override
    public Product updateProduct(String id, ProductRequestDTO requestDTO) {
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = productRequestToFakeStoreProductRequest(requestDTO);

        FakeStoreProductResponseDTO updatedFakeStoreProduct = fakeStoreAPIClient.updateProduct(Integer.parseInt(id), fakeStoreProductRequestDTO);

        redisTemplate.opsForHash().put("PRODUCTS", id, updatedFakeStoreProduct);

        return fakeStoreProductToProduct(updatedFakeStoreProduct);
    }

    @Override
    public Product modifyProduct(String id, ProductRequestDTO requestDTO) {
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = productRequestToFakeStoreProductRequest(requestDTO);

        FakeStoreProductResponseDTO modifiedFakeStoreProduct = fakeStoreAPIClient.updateProduct(Integer.parseInt(id), fakeStoreProductRequestDTO);

        redisTemplate.opsForHash().put("PRODUCTS", id, modifiedFakeStoreProduct);


        return fakeStoreProductToProduct(modifiedFakeStoreProduct);
    }
}
