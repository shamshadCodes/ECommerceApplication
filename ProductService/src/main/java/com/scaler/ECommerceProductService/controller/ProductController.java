package com.scaler.ECommerceProductService.controller;

import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import com.scaler.ECommerceProductService.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
public class ProductController {
    @GetMapping("/products")
    public ResponseEntity getAllProducts() {
        ProductResponseDTO product1 = new ProductResponseDTO();
        product1.setId(1);
        product1.setTitle("iPhone 12");
        product1.setDescription("Apple iPhone 12 (64GB) - Blue");
        product1.setCategory("Electronics");
        product1.setImage("https://images-na.ssl-images-amazon.com/images/I/71ZOtNdaZCL._SL1500_.jpg");
        product1.setPrice(799.00);

        ProductResponseDTO product2 = new ProductResponseDTO();
        product2.setId(2);
        product2.setTitle("OnePlus 8T");
        product2.setDescription("OnePlus 8T 5G (Aquamarine Green, 8GB RAM, 128GB Storage)");
        product2.setCategory("Electronics");
        product2.setImage("https://images-na.ssl-images-amazon.com/images/I/71m05O2uNdL._SL1500_.jpg");
        product2.setPrice(599.00);

        List<ProductResponseDTO> products = Arrays.asList(product1, product2);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/")
    public ResponseEntity getProductById(@RequestParam Integer id) {
        ProductService productService = new ProductService();
        ProductResponseDTO product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

}

