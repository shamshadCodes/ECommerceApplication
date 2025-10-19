package com.scaler.ECommerceProductService.dto.Request;

import lombok.Data;

@Data
public class ProductSearchRequest {
    private String name;
    private String category;
    private double minPrice;
    private double maxPrice;
    private String sortBy = "title";
    private String sortOrder = "asc";
    private int page = 0;
    private int pageSize = 10;
}
