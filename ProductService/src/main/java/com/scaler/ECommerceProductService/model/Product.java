package com.scaler.ECommerceProductService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Getter
@Setter
@Entity
public class Product extends BaseModel{
    private String name;
    private String description;
    private String image;
    private double price;
    @ManyToOne
    private Category category;
}
