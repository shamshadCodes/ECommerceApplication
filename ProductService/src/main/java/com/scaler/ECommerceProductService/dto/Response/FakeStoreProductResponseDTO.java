package com.scaler.ECommerceProductService.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FakeStoreProductResponseDTO implements Serializable {
    private int id;
    private String title;
    private String description;
    private String category;
    private String image;
    private double price;
}
