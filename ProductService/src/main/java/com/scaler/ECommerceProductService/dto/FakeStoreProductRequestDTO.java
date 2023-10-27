package com.scaler.ECommerceProductService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductRequestDTO {
    private String title;
    private String description;
    private String category;
    private String image;
    private double price;
}
