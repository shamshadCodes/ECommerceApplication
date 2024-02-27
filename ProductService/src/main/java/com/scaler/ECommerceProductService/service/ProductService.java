package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.exception.CategoryNotFoundException;
import com.scaler.ECommerceProductService.exception.ProductAlreadyExistsException;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;
import com.scaler.ECommerceProductService.exception.ProductServiceException;
import com.scaler.ECommerceProductService.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts() throws ProductServiceException;
    List<Product> getProductsByCategory(String category) throws ProductServiceException, CategoryNotFoundException;

    Product getProductById(String id) throws ProductNotFoundException;

    Product addProduct(ProductRequestDTO product) throws ProductAlreadyExistsException;

    Product deleteProduct(String id) throws ProductNotFoundException;

    Product updateProduct(String id, ProductRequestDTO product) throws ProductNotFoundException;

    Product modifyProduct(String id, ProductRequestDTO product) throws ProductNotFoundException;
}
