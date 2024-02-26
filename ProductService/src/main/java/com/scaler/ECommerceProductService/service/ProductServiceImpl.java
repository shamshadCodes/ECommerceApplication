package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.Repository.CategoryRepository;
import com.scaler.ECommerceProductService.Repository.ProductRepository;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;
import com.scaler.ECommerceProductService.exception.ProductServiceException;
import com.scaler.ECommerceProductService.model.Category;
import com.scaler.ECommerceProductService.model.Price;
import com.scaler.ECommerceProductService.model.Product;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Service("ProductServiceImpl")
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        }
        catch (DataAccessException e){
            throw new ProductServiceException("Error retrieving products", e);
        }
    }

    public Product getProductById(String id) throws ProductNotFoundException {
        Optional<Product> productOptional;

        try{
            productOptional = productRepository.findById(id);
        }catch (DataAccessException e){
            throw new ProductServiceException("Error retrieving the product", e);
        }
        if(productOptional.isEmpty()){
            throw new ProductNotFoundException("Product with id: " + id + " could not be found!");
        }
        return productOptional.get();
    }

    @Override
    public Product addProduct(ProductRequestDTO requestDTO) {
        Product product = new Product();
        String categoryName = requestDTO.getCategory();
        Category requestedCategory;

        Optional<Category> categoryOptional = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
        if(categoryOptional.isEmpty()){
            requestedCategory = new Category();
            requestedCategory.setCategoryName(requestDTO.getCategory());
        }
        else{
            requestedCategory = categoryOptional.get();
        }

        Price price = new Price();
        price.setPrice(requestDTO.getPrice());
        if(requestDTO.getCurrencyCode() == null){
            price.setCurrency(Currency.getInstance("INR"));
        }
        else{
            price.setCurrency(Currency.getInstance(requestDTO.getCurrencyCode()));
        }
        price.setDiscount(requestDTO.getDiscountPercentage());

        product.setCategory(requestedCategory);
        product.setPrice(price);
        product.setTitle(requestDTO.getTitle());
        product.setDescription(requestDTO.getDescription());
        product.setImage(requestDTO.getImage());

        Product savedProduct;

        try{
            savedProduct = productRepository.save(product);
        } catch (DataAccessException e){
            throw new ProductServiceException("Error saving the product!", e);
        }

        return savedProduct;
    }

    @Override
    public Product deleteProduct(String id) throws ProductNotFoundException {
        Optional<Product> productOptional;
        //Searching for the product
        try{
            productOptional = productRepository.findById(id);
        }catch (DataAccessException e){
            throw new ProductServiceException("Error Retrieving the product", e);
        }

        if(productOptional.isEmpty()){
            throw new ProductNotFoundException("Product with id: " + id + " could not be found!");
        }

        try{
            productRepository.deleteById(id);
        }catch (DataAccessException e){
            throw new ProductServiceException("Error retrieving the product", e);
        }

        return productOptional.get();
    }

    @Override
    public Product updateProduct(String id, ProductRequestDTO product) {
        return null;
    }

    @Override
    public Product modifyProduct(String id, ProductRequestDTO product) {
        return null;
    }
}
