package com.example.UserManagementService.controller;

import com.example.UserManagementService.dto.SetUserRolesRequestDTO;
import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.model.User;
import com.example.UserManagementService.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getUserDetails_ReturnsUserDTO() {
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = new UserDTO(new User());
        when(userService.getUserDetails(userId)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserDetails(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(userDTO, response.getBody());
        verify(userService, times(1)).getUserDetails(userId);
    }

    @Test
    void setRoles_DelegatesToServiceAndReturnsUserDTO() {
        UUID userId = UUID.randomUUID();
        SetUserRolesRequestDTO request = new SetUserRolesRequestDTO();
        request.setRoleIds(List.of(UUID.randomUUID()));

        UserDTO userDTO = new UserDTO(new User());
        when(userService.setUserRoles(eq(userId), anyList())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.setRoles(userId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(userDTO, response.getBody());
        verify(userService, times(1)).setUserRoles(userId, request.getRoleIds());
    }

    @Test
    void fetchAllUsers_ReturnsUsersFromService() {
        List<User> users = List.of(new User());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.fetchAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(users, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }
}
