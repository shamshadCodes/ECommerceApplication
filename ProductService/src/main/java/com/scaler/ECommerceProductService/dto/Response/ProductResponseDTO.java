package com.scaler.ECommerceProductService.dto.Response;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private String category;
    private String image;
    private double price;
}
