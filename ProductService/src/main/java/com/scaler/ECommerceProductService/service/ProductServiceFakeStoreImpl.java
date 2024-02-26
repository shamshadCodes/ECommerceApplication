package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.client.FakeStoreAPIClient;
import com.scaler.ECommerceProductService.dto.*;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;
import com.scaler.ECommerceProductService.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.scaler.ECommerceProductService.mapper.ProductMapper.productRequestToFakeStoreProductRequest;
import static com.scaler.ECommerceProductService.utils.ProductUtils.isNull;

@Service("ProductServiceFakeStoreImpl")
public class ProductServiceFakeStoreImpl implements ProductService {
    private final FakeStoreAPIClient fakeStoreAPIClient;

    public ProductServiceFakeStoreImpl(FakeStoreAPIClient fakeStoreAPIClient) {
        this.fakeStoreAPIClient = fakeStoreAPIClient;
    }

    @Override
    public List<Product> getAllProducts() {

        return fakeStoreAPIClient.getAllProducts();
    }

    @Override
    public Product getProductById(String id) throws ProductNotFoundException {
        Product fakeStoreProduct = fakeStoreAPIClient.getProductById(Integer.parseInt(id));
        if(isNull(fakeStoreProduct)){
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        return fakeStoreProduct;
    }

    @Override
    public Product addProduct(ProductRequestDTO productRequestDTO) {
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = productRequestToFakeStoreProductRequest(productRequestDTO);

        return fakeStoreAPIClient.createProduct(fakeStoreProductRequestDTO);
    }

    @Override
    public Product deleteProduct(Integer id) throws ProductNotFoundException {
        Product fakeStoreProduct = fakeStoreAPIClient.deleteProduct(id);
        if(isNull(fakeStoreProduct)){
            throw new ProductNotFoundException("Product to be deleted not found!!!");
        }
        return fakeStoreProduct;
    }

    @Override
    public Product updateProduct(Integer id, ProductRequestDTO product) {
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = productRequestToFakeStoreProductRequest(product);

        return fakeStoreAPIClient.updateProduct(id, fakeStoreProductRequestDTO);
    }

    @Override
    public Product modifyProduct(Integer id, ProductRequestDTO product) {
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = productRequestToFakeStoreProductRequest(product);

        return fakeStoreAPIClient.updateProduct(id, fakeStoreProductRequestDTO);
    }
}
