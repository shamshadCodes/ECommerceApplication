package com.example.userManagement.controller;

import com.example.userManagement.dto.CreateRoleRequestDTO;
import com.example.userManagement.model.Role;
import com.example.userManagement.service.RoleService;
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
