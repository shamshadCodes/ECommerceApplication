package com.scaler.ECommerceProductService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "ecom_order")
public class Order extends BaseModel{
    @ManyToMany
    private List<Product> products;
}
