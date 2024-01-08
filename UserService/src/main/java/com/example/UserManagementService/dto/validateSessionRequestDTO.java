package com.example.UserManagementService.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class validateSessionRequestDTO {
    private String token;
    private UUID userID;
}
