package com.example.userManagement.dto;

import com.example.userManagement.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SetUserRolesRequestDTO {
    private List<UUID> roleIds;
}
