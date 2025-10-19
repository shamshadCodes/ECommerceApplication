package com.scaler.ECommerceProductService.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO {
    private String message;
    private int messageCode;
}
