package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.client.FakeStoreAPIClient;
import com.scaler.ECommerceProductService.dto.*;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.scaler.ECommerceProductService.mapper.ProductMapper.fakeStoreProductResponseToProductResponse;
import static com.scaler.ECommerceProductService.mapper.ProductMapper.productRequestToFakeStoreProductRequest;
import static com.scaler.ECommerceProductService.utils.ProductUtils.isNull;

@Service("ProductServiceFakeStoreImpl")
public class ProductServiceFakeStoreImpl implements ProductService {
    private final FakeStoreAPIClient fakeStoreAPIClient;

    public ProductServiceFakeStoreImpl(FakeStoreAPIClient fakeStoreAPIClient) {
        this.fakeStoreAPIClient = fakeStoreAPIClient;
    }

    @Override
    public ProductListResponseDTO getAllProducts() {
        List<FakeStoreProductResponseDTO> fakeStoreProductList = fakeStoreAPIClient.getAllProducts();
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        for(FakeStoreProductResponseDTO fakeStoreProduct: fakeStoreProductList){
            productListResponseDTO.getProductList().add(fakeStoreProductResponseToProductResponse(fakeStoreProduct));
        }
        return productListResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) throws ProductNotFoundException {
        FakeStoreProductResponseDTO fakeStoreProductResponseDTO = fakeStoreAPIClient.getProductById(id);
        if(isNull(fakeStoreProductResponseDTO)){
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        return fakeStoreProductResponseToProductResponse(fakeStoreProductResponseDTO);
    }

    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = productRequestToFakeStoreProductRequest(productRequestDTO);
        FakeStoreProductResponseDTO fakeStoreProductResponseDTO = fakeStoreAPIClient.createProduct(fakeStoreProductRequestDTO);

        return fakeStoreProductResponseToProductResponse(fakeStoreProductResponseDTO);
    }

    @Override
    public ProductResponseDTO deleteProduct(Integer id) throws ProductNotFoundException {
        FakeStoreProductResponseDTO fakeStoreProductResponseDTO = fakeStoreAPIClient.deleteProduct(id);
        if(isNull(fakeStoreProductResponseDTO)){
            throw new ProductNotFoundException("Product to be deleted not found!!!");
        }
        return fakeStoreProductResponseToProductResponse(fakeStoreProductResponseDTO);
    }

    @Override
    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO product) {
        FakeStoreProductResponseDTO fakeStoreProductResponseDTO = fakeStoreAPIClient.updateProduct(id, productRequestToFakeStoreProductRequest(product));
        return fakeStoreProductResponseToProductResponse(fakeStoreProductResponseDTO);
    }

    @Override
    public ProductResponseDTO modifyProduct(Integer id, ProductRequestDTO product) {
        FakeStoreProductResponseDTO fakeStoreProductResponseDTO = fakeStoreAPIClient.modifyProduct(id, productRequestToFakeStoreProductRequest(product));
        return fakeStoreProductResponseToProductResponse(fakeStoreProductResponseDTO);
    }
}
