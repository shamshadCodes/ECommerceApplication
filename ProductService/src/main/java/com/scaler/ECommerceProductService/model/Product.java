package com.scaler.ECommerceProductService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "products")
public class Product extends BaseModel{
    private String title;
    @Column(length = 1000)
    private String description;
    private String image;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Price price;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;
}
