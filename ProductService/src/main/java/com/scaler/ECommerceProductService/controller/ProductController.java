package com.scaler.ECommerceProductService.controller;

import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;
import com.scaler.ECommerceProductService.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(@Qualifier("ProductServiceFakeStoreImpl") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity getAllProducts() {
        ProductListResponseDTO products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity getProductById(@PathVariable Integer id) throws ProductNotFoundException {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/products")
    public ResponseEntity addProduct(@RequestBody ProductRequestDTO product) {
        ProductResponseDTO productResponse = productService.addProduct(product);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable Integer id) {
        boolean productResponse = productService.deleteProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id, @RequestBody ProductRequestDTO product) {
        ProductResponseDTO productResponse = productService.updateProduct(id, product);
        return ResponseEntity.ok(productResponse);
    }
}

