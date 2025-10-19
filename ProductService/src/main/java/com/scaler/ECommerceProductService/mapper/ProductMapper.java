package com.scaler.ECommerceProductService.mapper;

import com.scaler.ECommerceProductService.dto.Request.FakeStoreProductRequestDTO;
import com.scaler.ECommerceProductService.dto.Request.ProductRequestDTO;
import com.scaler.ECommerceProductService.dto.Response.FakeStoreProductResponseDTO;
import com.scaler.ECommerceProductService.dto.Response.ProductListResponseDTO;
import com.scaler.ECommerceProductService.dto.Response.ProductResponseDTO;
import com.scaler.ECommerceProductService.model.Category;
import com.scaler.ECommerceProductService.model.Price;
import com.scaler.ECommerceProductService.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public class ProductMapper {

    public static FakeStoreProductRequestDTO productRequestToFakeStoreProductRequest(ProductRequestDTO productRequestDTO){
        FakeStoreProductRequestDTO fakeStoreProductRequestDTO = new FakeStoreProductRequestDTO();
        fakeStoreProductRequestDTO.setTitle(productRequestDTO.getTitle());
        fakeStoreProductRequestDTO.setPrice(productRequestDTO.getPrice());
        fakeStoreProductRequestDTO.setCategory(productRequestDTO.getCategory());
        fakeStoreProductRequestDTO.setImage(productRequestDTO.getImage());
        fakeStoreProductRequestDTO.setDescription(productRequestDTO.getDescription());

        return fakeStoreProductRequestDTO;
    }

    public static Product fakeStoreProductToProduct(FakeStoreProductResponseDTO fakeStoreProduct){
        Product product = new Product();
        Price price = new Price();
        Category category = new Category();

        price.setPrice(fakeStoreProduct.getPrice());

        category.setCategoryName(fakeStoreProduct.getCategory());

//        product.setId(String.valueOf(fakeStoreProduct.getId()));
        product.setTitle(fakeStoreProduct.getTitle());
        product.setImage(fakeStoreProduct.getImage());
        product.setDescription(fakeStoreProduct.getDescription());
        product.setPrice(price);
        product.setCategory(category);

        return product;
    }

    public static ProductResponseDTO productToProductResponseDTO(Product product){
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(product.getId());
        productResponseDTO.setTitle(product.getTitle());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setImage(product.getImage());
        productResponseDTO.setCategory(product.getCategory().getCategoryName());
        productResponseDTO.setPrice(product.getPrice().getPrice());

        return productResponseDTO;
    }

    public static ProductListResponseDTO productListToProductListResponseDTO(List<Product> productList){
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        for(Product product: productList){
            productListResponseDTO.getProductList()
                    .add(productToProductResponseDTO(product));
        }

        return productListResponseDTO;
    }

    public static ProductListResponseDTO productListToProductListResponseDTO(Page<Product> productPage){
        ProductListResponseDTO productListResponseDTO = new ProductListResponseDTO();
        for(Product product: productPage.getContent()){
            productListResponseDTO.getProductList()
                    .add(productToProductResponseDTO(product));
        }

        productListResponseDTO.setTotalProducts(productPage.getTotalElements());
        productListResponseDTO.setTotalPages(productPage.getTotalPages());
        productListResponseDTO.setPageSize(productPage.getSize());
        productListResponseDTO.setPageNumber(productPage.getNumber());

        return productListResponseDTO;
    }

    public static ProductRequestDTO fakeStoreProductToProductRequestDTO(FakeStoreProductResponseDTO fakeStoreProduct){
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setTitle(fakeStoreProduct.getTitle());
        requestDTO.setDescription(fakeStoreProduct.getDescription());
        requestDTO.setImage(fakeStoreProduct.getImage());
        requestDTO.setPrice(fakeStoreProduct.getPrice());
        requestDTO.setCategory(fakeStoreProduct.getCategory());

        return requestDTO;
    }

}
