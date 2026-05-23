package com.example.UserManagementService.service;

import com.example.UserManagementService.model.Role;
import com.example.UserManagementService.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void createRole_PersistsRoleAndReturnsIt() {
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Role result = roleService.createRole("ADMIN");

        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void getAllRoles_ReturnsRolesFromRepository() {
        Role role = new Role();
        role.setName("USER");

        when(roleRepository.findAll()).thenReturn(List.of(role));

        List<Role> result = roleService.getAllRoles();

        assertEquals(1, result.size());
        assertEquals("USER", result.get(0).getName());
        verify(roleRepository, times(1)).findAll();
    }
}
