package com.scaler.ECommerceProductService.controller;

import com.scaler.ECommerceProductService.dto.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.ProductResponseDTO;
import com.scaler.ECommerceProductService.exception.CategoryNotFoundException;
import com.scaler.ECommerceProductService.exception.ProductAlreadyExistsException;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;
import com.scaler.ECommerceProductService.model.Product;
import com.scaler.ECommerceProductService.service.ProductService;

import jakarta.validation.Valid;
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

    @GetMapping("/")
    public ResponseEntity<ProductListResponseDTO> getProductsByCategory(@RequestParam String category) throws CategoryNotFoundException {
        List<Product> products = productService.getProductsByCategory(category);
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
    public ResponseEntity<ProductResponseDTO> addProduct(@Valid @RequestBody ProductRequestDTO product) throws ProductAlreadyExistsException {
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
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequestDTO requestDTO) throws ProductNotFoundException {
        Product updatedProduct = productService.updateProduct(id, requestDTO);
        ProductResponseDTO productResponseDTO = productToProductResponseDTO(updatedProduct);

        return ResponseEntity.ok(productResponseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> modifyProduct(@PathVariable String id, @RequestBody ProductRequestDTO requestDTO) throws ProductNotFoundException {
        Product modifiedProduct = productService.modifyProduct(id, requestDTO);
        ProductResponseDTO productResponseDTO = productToProductResponseDTO(modifiedProduct);

        return ResponseEntity.ok(productResponseDTO);
    }
}

