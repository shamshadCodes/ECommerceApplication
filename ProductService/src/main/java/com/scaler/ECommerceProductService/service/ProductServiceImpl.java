package com.scaler.ECommerceProductService.service;

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
import java.util.UUID;

@Service("ProductServiceImpl")
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        String uuidFormattedString = id.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{12})",
                "$1-$2-$3-$4-$5"
        );

        System.out.print(UUID.fromString(uuidFormattedString));

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

        Category category = new Category();
        category.setCategoryName(requestDTO.getCategory());

        Price price = new Price();
        price.setPrice(requestDTO.getPrice());
        price.setCurrency(Currency.getInstance(requestDTO.getCurrencyCode()));
        price.setDiscount(requestDTO.getDiscountPercentage());

        product.setCategory(category);
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
    public Product deleteProduct(String id) {
        return null;
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
