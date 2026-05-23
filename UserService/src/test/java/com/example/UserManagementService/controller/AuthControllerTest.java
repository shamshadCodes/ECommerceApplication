package com.example.UserManagementService.controller;

import com.example.UserManagementService.dto.*;
import com.example.UserManagementService.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void signUp_DelegatesToAuthServiceAndReturnsOk() {
        SignUpRequestDTO request = new SignUpRequestDTO();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password");

        UserDTO userDTO = new UserDTO(new com.example.UserManagementService.model.User());
        ResponseEntity<UserDTO> expected = new ResponseEntity<>(userDTO, HttpStatus.OK);

        when(authService.signUpUser(anyString(), anyString(), anyString()))
                .thenReturn(userDTO);

        ResponseEntity<UserDTO> response = authController.signUp(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(authService, times(1))
                .signUpUser(request.getName(), request.getEmail(), request.getPassword());
    }

    @Test
    void login_ReturnsResponseFromAuthService() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password");

        SessionDTO sessionDTO = mock(SessionDTO.class);
        ResponseEntity<SessionDTO> expected = new ResponseEntity<>(sessionDTO, HttpStatus.OK);

        when(authService.loginUser(request.getEmail(), request.getPassword()))
                .thenReturn(expected);

        ResponseEntity<SessionDTO> response = authController.login(request);

        assertSame(expected, response);
        verify(authService, times(1))
                .loginUser(request.getEmail(), request.getPassword());
    }

    @Test
    void logout_CallsAuthServiceAndReturnsOk() {
        UUID userId = UUID.randomUUID();
        String token = "test-token";

        ResponseEntity<Void> response = authController.logout(userId, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService, times(1)).logoutUser(userId, token);
    }

    @Test
    void validateSession_ReturnsSessionDTO() {
        UUID userId = UUID.randomUUID();
        String token = "test-token";

        SessionDTO sessionDTO = mock(SessionDTO.class);
        when(authService.validateSession(userId, token)).thenReturn(sessionDTO);

        ResponseEntity<SessionDTO> response = authController.validateSession(userId, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(sessionDTO, response.getBody());
        verify(authService, times(1)).validateSession(userId, token);
    }
}
