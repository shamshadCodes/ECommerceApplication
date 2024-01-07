package com.example.UserManagementService.controller;

import com.example.UserManagementService.dto.CreateRoleRequestDTO;
import com.example.UserManagementService.model.Role;
import com.example.UserManagementService.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody CreateRoleRequestDTO roleRequestDTO){
        Role role = roleService.createRole(roleRequestDTO.getName());
        return ResponseEntity.ok(role);
    }

}
