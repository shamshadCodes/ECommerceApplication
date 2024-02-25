package com.scaler.ECommerceProductService.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Currency;

@Getter
@Setter
@Entity
public class Price extends BaseModel{
    private Currency currency;
    private double price;
    private double discount;
}
