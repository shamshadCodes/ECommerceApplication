package com.scaler.ECommerceProductService.controller;

import com.scaler.ECommerceProductService.dto.Request.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.Request.ProductSearchRequest;
import com.scaler.ECommerceProductService.dto.Response.CategoryListResponseDTO;
import com.scaler.ECommerceProductService.dto.Response.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.Response.ProductResponseDTO;
import com.scaler.ECommerceProductService.model.Category;
import com.scaler.ECommerceProductService.model.Price;
import com.scaler.ECommerceProductService.model.Product;
import com.scaler.ECommerceProductService.service.InitService;
import com.scaler.ECommerceProductService.service.ProductService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private InitService initService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(UUID.randomUUID());
        category.setCategoryName("Electronics");

        Price price = new Price();
        price.setId(UUID.randomUUID());
        price.setPrice(999.99);
        price.setCurrency(Currency.getInstance("USD"));

        product = new Product();
        product.setId(UUID.randomUUID());
        product.setTitle("Test Product");
        product.setDescription("Test Description");
        product.setImage("image.jpg");
        product.setCategory(category);
        product.setPrice(price);
    }

    @Test
    void getAllProducts_ReturnsMappedResponse() {
        when(productService.getAllProducts()).thenReturn(List.of(product));

        ResponseEntity<ProductListResponseDTO> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getProductList().size());
        ProductResponseDTO dto = response.getBody().getProductList().get(0);
        assertEquals(product.getTitle(), dto.getTitle());
        assertEquals(category.getCategoryName(), dto.getCategory());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getAllCategories_ReturnsMappedResponse() {
        when(productService.getAllCategories()).thenReturn(List.of(category));

        ResponseEntity<CategoryListResponseDTO> response = productController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getCategoryResponseDTOList().size());
        assertEquals(category.getCategoryName(),
                response.getBody().getCategoryResponseDTOList().get(0).getCategoryName());
        verify(productService, times(1)).getAllCategories();
    }

    @Test
    void getProductsByCategory_DelegatesToService() throws Exception {
        when(productService.getProductsByCategory("Electronics"))
                .thenReturn(List.of(product));

        ResponseEntity<ProductListResponseDTO> response =
                productController.getProductsByCategory("Electronics");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getProductList().size());
        verify(productService, times(1)).getProductsByCategory("Electronics");
    }

    @Test
    void getProductById_ReturnsMappedResponse() throws Exception {
        when(productService.getProductById(anyString())).thenReturn(product);

        ResponseEntity<ProductResponseDTO> response =
                productController.getProductById(product.getId().toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(product.getTitle(), response.getBody().getTitle());
        verify(productService, times(1)).getProductById(product.getId().toString());
    }

    @Test
    void addProduct_DelegatesToServiceAndReturnsResponse() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setTitle("New Product");
        request.setCategory("Electronics");
        request.setPrice(500.0);

        when(productService.addProduct(any(ProductRequestDTO.class))).thenReturn(product);

        ResponseEntity<ProductResponseDTO> response = productController.addProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(productService, times(1)).addProduct(request);
    }

    @Test
    void deleteProduct_DelegatesToServiceAndReturnsResponse() throws Exception {
        when(productService.deleteProduct(anyString())).thenReturn(product);

        ResponseEntity<ProductResponseDTO> response =
                productController.deleteProduct(product.getId().toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(product.getId().toString());
    }

    @Test
    void updateProduct_DelegatesToServiceAndReturnsResponse() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setTitle("Updated Title");

        when(productService.updateProduct(anyString(), any(ProductRequestDTO.class)))
                .thenReturn(product);

        ResponseEntity<ProductResponseDTO> response =
                productController.updateProduct(product.getId().toString(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService, times(1))
                .updateProduct(product.getId().toString(), request);
    }

    @Test
    void modifyProduct_DelegatesToServiceAndReturnsResponse() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setTitle("Patched Title");

        when(productService.modifyProduct(anyString(), any(ProductRequestDTO.class)))
                .thenReturn(product);

        ResponseEntity<ProductResponseDTO> response =
                productController.modifyProduct(product.getId().toString(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService, times(1))
                .modifyProduct(product.getId().toString(), request);
    }

    @Test
    void copyProductsFromFakeStore_UsesInitServiceAndReturnsMappedResponse() throws Exception {
        when(initService.copyProductsFromFakeStore()).thenReturn(List.of(product));

        ResponseEntity<ProductListResponseDTO> response =
                productController.copyProductsFromFakeStore();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getProductList().size());
        verify(initService, times(1)).copyProductsFromFakeStore();
    }

    @Test
    void searchProducts_BuildsPageableAndReturnsMappedResponse() {
        ProductSearchRequest request = new ProductSearchRequest();
        request.setName("Test");
        request.setCategory("Electronics");
        request.setSortBy("title");
        request.setSortOrder("asc");
        request.setPage(0);
        request.setPageSize(10);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);

        when(productService.searchProducts(eq(request), any(Pageable.class)))
                .thenReturn(page);

        ResponseEntity<ProductListResponseDTO> response = productController.searchProducts(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getProductList().size());
        verify(productService, times(1))
                .searchProducts(eq(request), any(Pageable.class));
    }
}
