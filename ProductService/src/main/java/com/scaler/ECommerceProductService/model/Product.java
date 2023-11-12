package com.scaler.ECommerceProductService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Getter
@Setter
@Entity
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private String image;
    private double price;
}
