package com.scaler.ECommerceProductService.controller;

import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import com.scaler.ECommerceProductService.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    ProductResponseDTO product2 = new ProductResponseDTO();

    @Autowired
    @Qualifier("ProductServiceFakeStoreImpl")
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity getProductById(@PathVariable Integer id) {
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

}

