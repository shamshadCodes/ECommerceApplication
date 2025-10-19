package com.scaler.ECommerceProductService.dto.Response;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductListResponseDTO {
    private List<ProductResponseDTO> productList;
    private long totalProducts;
    private int totalPages;
    private int pageSize;
    private int pageNumber;


    public ProductListResponseDTO(){
        this.productList = new ArrayList<>();
    }
}
