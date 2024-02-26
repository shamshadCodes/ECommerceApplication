package com.scaler.ECommerceProductService.controller;

import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;
import com.scaler.ECommerceProductService.model.Product;
import com.scaler.ECommerceProductService.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.scaler.ECommerceProductService.mapper.ProductMapper.productListToProductListResponseDTO;
import static com.scaler.ECommerceProductService.mapper.ProductMapper.productToProductResponseDTO;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(@Qualifier("ProductServiceImpl") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductListResponseDTO> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        ProductListResponseDTO productListResponseDTO = productListToProductListResponseDTO(products);
        return ResponseEntity.ok(productListResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable String id) throws ProductNotFoundException {
        Product product = productService.getProductById(id);

        ProductResponseDTO productResponseDTO = productToProductResponseDTO(product);

        return ResponseEntity.ok(productResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> addProduct(@RequestBody ProductRequestDTO product) {
        Product savedProduct = productService.addProduct(product);
        ProductResponseDTO productResponseDTO = productToProductResponseDTO(savedProduct);

        return ResponseEntity.ok(productResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable String id) throws ProductNotFoundException {
        Product deletedProduct = productService.deleteProduct(id);
        ProductResponseDTO productResponseDTO = productToProductResponseDTO(deletedProduct);

        return ResponseEntity.ok(productResponseDTO);
    }

    //TODO: Check and fix the PUT and PATCH mappings
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable String id, @RequestBody ProductRequestDTO product) {
        Product updatedProduct = productService.updateProduct(id, product);
        ProductResponseDTO productResponseDTO = productToProductResponseDTO(updatedProduct);

        return ResponseEntity.ok(productResponseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> modifyProduct(@PathVariable String id, @RequestBody ProductRequestDTO product) {
        Product modifiedProduct = productService.modifyProduct(id, product);
        ProductResponseDTO productResponseDTO = productToProductResponseDTO(modifiedProduct);

        return ResponseEntity.ok(productResponseDTO);
    }
}

