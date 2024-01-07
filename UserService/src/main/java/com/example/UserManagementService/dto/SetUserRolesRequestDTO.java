package com.example.UserManagementService.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SetUserRolesRequestDTO {
    private List<UUID> roleIds;
}
