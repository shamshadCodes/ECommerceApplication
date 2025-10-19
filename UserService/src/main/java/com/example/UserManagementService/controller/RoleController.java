package com.example.UserManagementService.controller;

import com.example.UserManagementService.dto.CreateRoleRequestDTO;
import com.example.UserManagementService.model.Role;
import com.example.UserManagementService.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody CreateRoleRequestDTO roleRequestDTO){
        Role role = roleService.createRole(roleRequestDTO.getName());
        return ResponseEntity.ok(role);
    }

    @GetMapping
    public ResponseEntity<List<Role>> fetchAllRoles(){
        List<Role> role = roleService.getAllRoles();
        return ResponseEntity.ok(role);
    }

}
