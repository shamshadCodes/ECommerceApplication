package com.scaler.ECommerceProductService.controller;

import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import com.scaler.ECommerceProductService.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/products/")
    public ResponseEntity getProductById(@RequestParam Integer id) {
        ProductResponseDTO product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

}

