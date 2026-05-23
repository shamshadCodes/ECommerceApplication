package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.Repository.CategoryRepository;
import com.scaler.ECommerceProductService.Repository.ProductRepository;
import com.scaler.ECommerceProductService.dto.Request.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.Request.ProductSearchRequest;
import com.scaler.ECommerceProductService.exception.CategoryNotFoundException;
import com.scaler.ECommerceProductService.exception.ProductAlreadyExistsException;
import com.scaler.ECommerceProductService.exception.ProductNotFoundException;
import com.scaler.ECommerceProductService.model.Category;
import com.scaler.ECommerceProductService.model.Price;
import com.scaler.ECommerceProductService.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    private Category testCategory;
    private Price testPrice;
    private ProductRequestDTO testRequestDTO;

    @BeforeEach
    void setUp() {
        // Setup test category
        testCategory = new Category();
        testCategory.setId(UUID.randomUUID());
        testCategory.setCategoryName("Electronics");

        // Setup test price
        testPrice = new Price();
        testPrice.setId(UUID.randomUUID());
        testPrice.setPrice(999.99);
        testPrice.setCurrency(Currency.getInstance("USD"));
        testPrice.setDiscount(10.0);

        // Setup test product
        testProduct = new Product();
        testProduct.setId(UUID.randomUUID());
        testProduct.setTitle("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setImage("test.jpg");
        testProduct.setPrice(testPrice);
        testProduct.setCategory(testCategory);

        // Setup test request DTO
        testRequestDTO = new ProductRequestDTO();
        testRequestDTO.setTitle("New Product");
        testRequestDTO.setDescription("New Description");
        testRequestDTO.setCategory("Electronics");
        testRequestDTO.setImage("new.jpg");
        testRequestDTO.setPrice(499.99);
        testRequestDTO.setCurrencyCode("USD");
        testRequestDTO.setDiscountPercentage(5.0);
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct.getTitle(), result.get(0).getTitle());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        // Arrange
        List<Category> categories = Arrays.asList(testCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<Category> result = productService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCategory.getCategoryName(), result.get(0).getCategoryName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getProductById_WithValidId_ShouldReturnProduct() throws ProductNotFoundException {
        // Arrange
        String productId = testProduct.getId().toString();
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct.getTitle(), result.getTitle());
        assertEquals(testProduct.getDescription(), result.getDescription());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductById_WithInvalidId_ShouldThrowException() {
        // Arrange
        String invalidId = UUID.randomUUID().toString();
        when(productRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () ->
            productService.getProductById(invalidId)
        );
        verify(productRepository, times(1)).findById(invalidId);
    }

    @Test
    void addProduct_WithNewProduct_ShouldSaveProduct() throws ProductAlreadyExistsException {
        // Arrange
        when(productRepository.findByTitleIgnoreCase(testRequestDTO.getTitle())).thenReturn(Optional.empty());
        when(categoryRepository.findByCategoryNameIgnoreCase(testRequestDTO.getCategory()))
            .thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productService.addProduct(testRequestDTO);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findByTitleIgnoreCase(testRequestDTO.getTitle());
        verify(categoryRepository, times(1)).findByCategoryNameIgnoreCase(testRequestDTO.getCategory());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_WithExistingProduct_ShouldThrowException() {
        // Arrange
        when(productRepository.findByTitleIgnoreCase(testRequestDTO.getTitle()))
            .thenReturn(Optional.of(testProduct));

        // Act & Assert
        assertThrows(ProductAlreadyExistsException.class, () ->
            productService.addProduct(testRequestDTO)
        );
        verify(productRepository, times(1)).findByTitleIgnoreCase(testRequestDTO.getTitle());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_WithValidId_ShouldDeleteProduct() throws ProductNotFoundException {
        // Arrange
        String productId = testProduct.getId().toString();
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).deleteById(productId);

        // Act
        Product result = productService.deleteProduct(productId);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct.getId(), result.getId());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void deleteProduct_WithInvalidId_ShouldThrowException() {
        // Arrange
        String invalidId = UUID.randomUUID().toString();
        when(productRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () ->
            productService.deleteProduct(invalidId)
        );
        verify(productRepository, times(1)).findById(invalidId);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void updateProduct_WithValidId_ShouldUpdateProduct() throws ProductNotFoundException {
        // Arrange
        String productId = testProduct.getId().toString();
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(categoryRepository.findByCategoryNameIgnoreCase(testRequestDTO.getCategory()))
            .thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productService.updateProduct(productId, testRequestDTO);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void searchProducts_WithValidCriteria_ShouldReturnPage() {
        // Arrange
        ProductSearchRequest searchRequest = new ProductSearchRequest();
        searchRequest.setName("Test");
        searchRequest.setCategory("Electronics");
        searchRequest.setMinPrice(0);
        searchRequest.setMaxPrice(1000);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> expectedPage = new PageImpl<>(Arrays.asList(testProduct), pageable, 1);

        when(productRepository.searchProducts(
            eq("Test"), eq("Electronics"), eq(null), eq(1000.0), any(Pageable.class)))
            .thenReturn(expectedPage);

        // Act
        Page<Product> result = productService.searchProducts(searchRequest, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testProduct.getTitle(), result.getContent().get(0).getTitle());
    }

    @Test
    void getProductsByCategory_WithValidCategory_ShouldReturnProducts() throws Exception {
        // Arrange
        String categoryName = "Electronics";
        when(categoryRepository.findByCategoryNameIgnoreCase(categoryName))
            .thenReturn(Optional.of(testCategory));
        when(productRepository.findAllByCategory(testCategory))
            .thenReturn(Arrays.asList(testProduct));

        // Act
        List<Product> result = productService.getProductsByCategory(categoryName);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct.getTitle(), result.get(0).getTitle());
        verify(categoryRepository, times(1)).findByCategoryNameIgnoreCase(categoryName);
    }

    @Test
    void getProductsByCategory_WithInvalidCategory_ShouldThrowException() {
        // Arrange
        String invalidCategory = "InvalidCategory";
        when(categoryRepository.findByCategoryNameIgnoreCase(invalidCategory))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () ->
            productService.getProductsByCategory(invalidCategory)
        );
        verify(categoryRepository, times(1)).findByCategoryNameIgnoreCase(invalidCategory);
    }

    @Test
    void modifyProduct_WithValidId_ShouldModifyProduct() throws ProductNotFoundException {
        // Arrange
        String productId = testProduct.getId().toString();
        ProductRequestDTO partialUpdate = new ProductRequestDTO();
        partialUpdate.setTitle("Updated Title");
        partialUpdate.setPrice(599.99);
        partialUpdate.setCurrencyCode("USD");

        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(categoryRepository.findByCategoryNameIgnoreCase(null))
            .thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productService.modifyProduct(productId, partialUpdate);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
