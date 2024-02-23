package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.Repository.ProductRepository;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductServiceImpl")
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = productRepository.findAll();

        return null;
    }

    @Override
    public Product getProductById(Integer id) {
        return null;
    }

    @Override
    public Product addProduct(ProductRequestDTO product) {
        return null;
    }

    @Override
    public Product deleteProduct(Integer id) {
        return null;
    }

    @Override
    public Product updateProduct(Integer id, ProductRequestDTO product) {
        return null;
    }

    @Override
    public Product modifyProduct(Integer id, ProductRequestDTO product) {
        return null;
    }
}
