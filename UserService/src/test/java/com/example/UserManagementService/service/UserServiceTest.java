package com.example.UserManagementService.service;

import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.exception.UserNotFoundException;
import com.example.UserManagementService.model.Role;
import com.example.UserManagementService.model.User;
import com.example.UserManagementService.repository.RoleRepository;
import com.example.UserManagementService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");

        roleUser = new Role();
        roleUser.setId(UUID.randomUUID());
        roleUser.setName("USER");
    }

    @Test
    void getUserDetails_WithExistingUser_ReturnsUserDTO() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserDetails(userId);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserDetails_WithMissingUser_ThrowsUserNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserDetails(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void setUserRoles_WithValidUser_SetsRolesAndReturnsDTO() {
        List<UUID> roleIds = List.of(roleUser.getId());
        Set<Role> roles = new HashSet<>(Collections.singletonList(roleUser));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findRolesByIdIn(roleIds)).thenReturn(roles);

        UserDTO result = userService.setUserRoles(userId, roleIds);

        assertNotNull(result);
        assertEquals(1, result.getRoles().size());
        assertTrue(result.getRoles().stream().anyMatch(r -> "USER".equals(r.getName())));
        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, times(1)).findRolesByIdIn(roleIds);
    }

    @Test
    void setUserRoles_WithMissingUser_ThrowsUserNotFoundException() {
        List<UUID> roleIds = List.of(roleUser.getId());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.setUserRoles(userId, roleIds));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllUsers_ReturnsUsersFromRepository() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }
}
