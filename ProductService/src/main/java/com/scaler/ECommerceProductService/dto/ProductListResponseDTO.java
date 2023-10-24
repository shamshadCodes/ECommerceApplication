package com.scaler.ECommerceProductService.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductListResponseDTO {
    private List<ProductResponseDTO> productList;

    public void setProductList(List<ProductResponseDTO> productList) {
        this.productList = productList;
    }
}
