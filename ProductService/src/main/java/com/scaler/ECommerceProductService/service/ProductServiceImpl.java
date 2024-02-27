package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.Repository.CategoryRepository;
import com.scaler.ECommerceProductService.Repository.ProductRepository;
import com.scaler.ECommerceProductService.dto.ProductRequestDTO;
import com.scaler.ECommerceProductService.exception.CategoryNotFoundException;
import com.scaler.ECommerceProductService.exception.ProductAlreadyExistsException;
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
        return fetchProduct(id);
    }

    @Override
    public Product addProduct(ProductRequestDTO requestDTO) throws ProductAlreadyExistsException {
        Product product = new Product();
        Optional<Product> productOptional = productRepository.findByTitleIgnoreCase(requestDTO.getTitle());
        if(productOptional.isPresent()){
            throw new ProductAlreadyExistsException("The product you are trying to save already exists!");
        }

        Category requestedCategory = getRequestedCategory(requestDTO.getCategory());

        Price price = new Price();
        price.setPrice(requestDTO.getPrice());
        price.setCurrency(Currency.getInstance(requestDTO.getCurrencyCode().toUpperCase()));
        price.setDiscount(requestDTO.getDiscountPercentage());

        product.setCategory(requestedCategory);
        product.setPrice(price);
        product.setTitle(requestDTO.getTitle());
        product.setDescription(requestDTO.getDescription());
        product.setImage(requestDTO.getImage());

        return saveProduct(product);
    }

    @Override
    public Product deleteProduct(String id) throws ProductNotFoundException {
        Product product = fetchProduct(id);

        try{
            productRepository.deleteById(id);
        }catch (DataAccessException e){
            throw new ProductServiceException("Error retrieving the product", e);
        }

        return product;
    }

    @Override
    public Product updateProduct(String id, ProductRequestDTO requestDTO) throws ProductNotFoundException {
        Product product = fetchProduct(id);

        Category requestedCategory = getRequestedCategory(requestDTO.getCategory());

        Price updatedPrice = getRequestedPrice(requestDTO, product.getPrice());

        product.setTitle(requestDTO.getTitle());
        product.setImage(requestDTO.getImage());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(updatedPrice);
        product.setCategory(requestedCategory);

        return saveProduct(product);
    }

    @Override
    public Product modifyProduct(String id, ProductRequestDTO requestDTO) throws ProductNotFoundException {
        Product currentProduct = fetchProduct(id);

        Category updatedCategory;
        Optional<Category> categoryOptional = categoryRepository.findByCategoryNameIgnoreCase(requestDTO.getCategory());

        if(requestDTO.getCategory() == null
                || requestDTO.getCategory().isBlank()
                || (categoryOptional.isPresent() && categoryOptional.get().equals(currentProduct.getCategory()))
        ){
            updatedCategory = currentProduct.getCategory();
        }
        else{
            if(categoryOptional.isPresent()){
                updatedCategory = categoryOptional.get();
            }
            else{
                updatedCategory = new Category();
                updatedCategory.setCategoryName(requestDTO.getCategory());
            }
        }

        Price updatedPrice = getRequestedPrice(requestDTO, currentProduct.getPrice());

        currentProduct.setCategory(updatedCategory);
        currentProduct.setPrice(updatedPrice);

        if(requestDTO.getTitle() != null
                && !requestDTO.getTitle().isBlank()
                && requestDTO.getTitle().trim().length() >= 3) {
            currentProduct.setTitle(requestDTO.getTitle());
        }

        if(requestDTO.getDescription() != null
                && !requestDTO.getDescription().isBlank()
                && requestDTO.getDescription().trim().length() >= 5){
            currentProduct.setDescription(requestDTO.getDescription());
        }

        if(!requestDTO.getImage().equalsIgnoreCase("placeholder.jpg")
                && !requestDTO.getImage().isBlank()
                && !requestDTO.getImage().equalsIgnoreCase(currentProduct.getImage())){
            currentProduct.setImage(requestDTO.getImage());
        }

        return saveProduct(currentProduct);
    }

    public List<Product> getProductsByCategory(String categoryName) throws CategoryNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
        if(categoryOptional.isEmpty()){
            throw new CategoryNotFoundException("Category: " + categoryName + " could not be found!");
        }

        try {
            return productRepository.findAllByCategory(categoryOptional.get());
        }
        catch (DataAccessException e){
            throw new ProductServiceException("Error retrieving products", e);
        }

    }

    private Product fetchProduct(String id) throws ProductNotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new ProductNotFoundException("Product with id: " + id + " could not be found!");
        }

        return productOptional.get();
    }

    private static Price getRequestedPrice(ProductRequestDTO requestDTO, Price currentPrice) {
        Price updatedPrice;

        if(requestDTO.getPrice() > 0 &&
                (!currentPrice.getCurrency().getCurrencyCode().equalsIgnoreCase(requestDTO.getCurrencyCode())
                        || currentPrice.getPrice() != requestDTO.getPrice()
                        || currentPrice.getDiscount() != requestDTO.getDiscountPercentage())
        ){
            updatedPrice = new Price();
            updatedPrice.setPrice(requestDTO.getPrice());
            updatedPrice.setCurrency(Currency.getInstance(requestDTO.getCurrencyCode().toUpperCase()));
            updatedPrice.setDiscount(requestDTO.getDiscountPercentage());
        }
        else{
            updatedPrice = currentPrice;
        }
        return updatedPrice;
    }

    private Category getRequestedCategory(String categoryName) {
        Category requestedCategory;

        Optional<Category> categoryOptional = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
        if(categoryOptional.isEmpty()){
            requestedCategory = new Category();
            requestedCategory.setCategoryName(categoryName);
        }
        else{
            requestedCategory = categoryOptional.get();
        }
        return requestedCategory;
    }

    private Product saveProduct(Product product) {
        try{
            return productRepository.save(product);
        } catch (DataAccessException e){
            throw new ProductServiceException("Error while saving the product!", e);
        }
    }
}
