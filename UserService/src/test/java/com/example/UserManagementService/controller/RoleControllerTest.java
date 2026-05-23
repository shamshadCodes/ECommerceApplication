package com.example.UserManagementService.controller;

import com.example.UserManagementService.dto.CreateRoleRequestDTO;
import com.example.UserManagementService.model.Role;
import com.example.UserManagementService.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @Test
    void createRole_DelegatesToServiceAndReturnsRole() {
        CreateRoleRequestDTO request = new CreateRoleRequestDTO();
        request.setName("ADMIN");

        Role role = new Role();
        role.setName("ADMIN");
        when(roleService.createRole("ADMIN")).thenReturn(role);

        ResponseEntity<Role> response = roleController.createRole(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(role, response.getBody());
        verify(roleService, times(1)).createRole("ADMIN");
    }

    @Test
    void fetchAllRoles_ReturnsRolesFromService() {
        List<Role> roles = List.of(new Role());
        when(roleService.getAllRoles()).thenReturn(roles);

        ResponseEntity<List<Role>> response = roleController.fetchAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(roles, response.getBody());
        verify(roleService, times(1)).getAllRoles();
    }
}
