package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.client.FakeStoreAPIClient;
import com.scaler.ECommerceProductService.dto.Response.FakeStoreProductResponseDTO;
import com.scaler.ECommerceProductService.dto.Request.ProductRequestDTO;
import com.scaler.ECommerceProductService.exception.ProductAlreadyExistsException;
import com.scaler.ECommerceProductService.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.scaler.ECommerceProductService.mapper.ProductMapper.fakeStoreProductToProductRequestDTO;

@Service
@Transactional
public class InitService {
    private final FakeStoreAPIClient fakeStoreAPIClient;
    private final ProductServiceImpl productService;

    public InitService(FakeStoreAPIClient fakeStoreAPIClient, ProductServiceImpl productService) {
        this.fakeStoreAPIClient = fakeStoreAPIClient;
        this.productService = productService;
    }

    //    *****Fetches Products from the fakeStoreAPI and adds them to project's DB****
    public List<Product> copyProductsFromFakeStore() throws ProductAlreadyExistsException {
        List<FakeStoreProductResponseDTO> fakeStoreProductList = fakeStoreAPIClient.getAllProducts();
        List<Product> savedProductList = new ArrayList<>();

        for(FakeStoreProductResponseDTO fakeStoreProduct: fakeStoreProductList){
            ProductRequestDTO requestDTO = fakeStoreProductToProductRequestDTO(fakeStoreProduct);
            savedProductList.add(productService.addProduct(requestDTO));

        }

        return savedProductList;
    }
}
