package com.scaler.ECommerceProductService.dto.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
    @NotBlank(message = "Title cannot be blank.")
    @Size(min = 3)
    private String title;

    @Size(min = 5, message = "Description must be at least 5 characters long.")
    private String description = "";

    @NotBlank(message = "Category cannot be blank.")
    private String category;

    private String image="placeholder.jpg";

    @DecimalMin(value = "0.01", message = "Price must be greater than 0.")
    private double price;

    @Size(min = 3, max = 3, message = "Currency code must be 3 characters long.")
    private String currencyCode = "INR";

    @Min(value = 0, message = "Discount percentage cannot be negative.")
    @Max(value = 100, message = "Discount percentage cannot exceed 100%.")
    private double discountPercentage;
}
