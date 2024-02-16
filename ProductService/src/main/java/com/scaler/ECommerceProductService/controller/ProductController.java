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

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(@Qualifier("ProductServiceFakeStoreImpl") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductListResponseDTO> getAllProducts() {
        ProductListResponseDTO products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer id) throws ProductNotFoundException {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> addProduct(@RequestBody ProductRequestDTO product) {
        ProductResponseDTO productResponse = productService.addProduct(product);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable Integer id) throws ProductNotFoundException {
        ProductResponseDTO productResponse = productService.deleteProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    //TODO: Check and fix the PUT and PATCH mappings
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Integer id, @RequestBody ProductRequestDTO product) {
        ProductResponseDTO productResponse = productService.updateProduct(id, product);
        return ResponseEntity.ok(productResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> modifyProduct(@PathVariable Integer id, @RequestBody ProductRequestDTO product) {
        ProductResponseDTO productResponse = productService.modifyProduct(id, product);
        return ResponseEntity.ok(productResponse);
    }
}

