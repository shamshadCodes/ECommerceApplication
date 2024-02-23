package com.scaler.ECommerceProductService.mapper;

import com.scaler.ECommerceProductService.dto.*;
import com.scaler.ECommerceProductService.model.Category;
import com.scaler.ECommerceProductService.model.Price;
import com.scaler.ECommerceProductService.model.Product;

import java.util.List;

public class ProductMapper {
    public static ProductResponseDTO fakeStoreProductResponseToProductResponse(FakeStoreProductResponseDTO fakeStoreProductResponseDTO){
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(String.valueOf(fakeStoreProductResponseDTO.getId()));
        productResponseDTO.setTitle(fakeStoreProductResponseDTO.getTitle());
        productResponseDTO.setPrice(fakeStoreProductResponseDTO.getPrice());
        productResponseDTO.setCategory(fakeStoreProductResponseDTO.getCategory());
        productResponseDTO.setImage(fakeStoreProductResponseDTO.getImage());
        productResponseDTO.setDescription(fakeStoreProductResponseDTO.getDescription());

        return productResponseDTO;
    }

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

        product.setId(String.valueOf(fakeStoreProduct.getId()));
        product.setTitle(fakeStoreProduct.getTitle());
        product.setImage(fakeStoreProduct.getImage());
        product.setDescription(fakeStoreProduct.getDescription());
        product.setPrice(price);
        product.setCategory(category);

        return product;
    }

    public static  ProductResponseDTO productToProductResponseDTO(Product product){
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
}
