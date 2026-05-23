package com.example.UserManagementService.service;

import com.example.UserManagementService.dto.SessionDTO;
import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.exception.InvalidLoginCredentialsException;
import com.example.UserManagementService.exception.MaximumActiveSessionsException;
import com.example.UserManagementService.exception.SessionNotFoundException;
import com.example.UserManagementService.model.Session;
import com.example.UserManagementService.model.SessionStatus;
import com.example.UserManagementService.model.User;
import com.example.UserManagementService.repository.SessionRepository;
import com.example.UserManagementService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private Session testSession;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setHashedPassword("$2a$10$hashedPassword");

        testSession = new Session();
        testSession.setId(UUID.randomUUID());
        testSession.setUser(testUser);
        testSession.setToken("test-token");
        testSession.setSessionStatus(SessionStatus.ACTIVE);
        testSession.setIssuedAt(new Date());
        testSession.setExpiringAt(new Date(System.currentTimeMillis() + 86400000));
    }

    @Test
    void signUpUser_WithValidDetails_ShouldCreateUser() {
        // Arrange
        String name = "New User";
        String email = "new@example.com";
        String password = "password123";
        String hashedPassword = "$2a$10$hashedPassword";

        when(bCryptPasswordEncoder.encode(password)).thenReturn(hashedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserDTO result = authService.signUpUser(name, email, password);

        // Assert
        assertNotNull(result);
        verify(bCryptPasswordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void loginUser_WithValidCredentials_ShouldReturnSession() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(bCryptPasswordEncoder.matches(password, testUser.getHashedPassword())).thenReturn(true);
        when(sessionRepository.findAllByUserIdAndSessionStatus(testUser.getId(), SessionStatus.ACTIVE))
            .thenReturn(new HashSet<>());
        when(sessionRepository.save(any(Session.class))).thenReturn(testSession);

        // Act
        ResponseEntity<SessionDTO> result = authService.loginUser(email, password);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getBody());
        verify(userRepository, times(1)).findByEmail(email);
        verify(bCryptPasswordEncoder, times(1)).matches(password, testUser.getHashedPassword());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void loginUser_WithInvalidEmail_ShouldThrowException() {
        // Arrange
        String email = "invalid@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidLoginCredentialsException.class, () ->
            authService.loginUser(email, password)
        );
        verify(userRepository, times(1)).findByEmail(email);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void loginUser_WithInvalidPassword_ShouldThrowException() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongpassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(bCryptPasswordEncoder.matches(password, testUser.getHashedPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidLoginCredentialsException.class, () ->
            authService.loginUser(email, password)
        );
        verify(userRepository, times(1)).findByEmail(email);
        verify(bCryptPasswordEncoder, times(1)).matches(password, testUser.getHashedPassword());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void loginUser_WithMaxActiveSessions_ShouldThrowException() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        Set<Session> activeSessions = new HashSet<>();
        activeSessions.add(testSession);
        activeSessions.add(new Session());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(bCryptPasswordEncoder.matches(password, testUser.getHashedPassword())).thenReturn(true);
        when(sessionRepository.findAllByUserIdAndSessionStatus(testUser.getId(), SessionStatus.ACTIVE))
            .thenReturn(activeSessions);

        // Act & Assert
        assertThrows(MaximumActiveSessionsException.class, () ->
            authService.loginUser(email, password)
        );
    }

    @Test
    void logoutUser_WithValidSession_ShouldEndSession() {
        // Arrange
        UUID userId = testUser.getId();
        String token = "test-token";

        when(sessionRepository.findByTokenAndUserId(token, userId))
            .thenReturn(Optional.of(testSession));
        when(sessionRepository.save(any(Session.class))).thenReturn(testSession);

        // Act
        authService.logoutUser(userId, token);

        // Assert
        assertEquals(SessionStatus.ENDED, testSession.getSessionStatus());
        verify(sessionRepository, times(1)).findByTokenAndUserId(token, userId);
        verify(sessionRepository, times(1)).save(testSession);
    }

    @Test
    void logoutUser_WithInvalidSession_ShouldThrowException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String token = "invalid-token";

        when(sessionRepository.findByTokenAndUserId(token, userId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SessionNotFoundException.class, () ->
            authService.logoutUser(userId, token)
        );
        verify(sessionRepository, times(1)).findByTokenAndUserId(token, userId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void validateSession_WithValidToken_ShouldReturnSession() {
        // Arrange
        UUID userId = testUser.getId();
        String token = "test-token";

        when(sessionRepository.findByTokenAndUserId(token, userId))
            .thenReturn(Optional.of(testSession));

        // Act
        SessionDTO result = authService.validateSession(userId, token);

        // Assert
        assertNotNull(result);
        verify(sessionRepository, times(1)).findByTokenAndUserId(token, userId);
    }

    @Test
    void validateSession_WithInvalidToken_ShouldThrowException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String token = "invalid-token";

        when(sessionRepository.findByTokenAndUserId(token, userId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SessionNotFoundException.class, () ->
            authService.validateSession(userId, token)
        );
        verify(sessionRepository, times(1)).findByTokenAndUserId(token, userId);
    }

    @Test
    void signUpUser_ShouldHashPassword() {
        // Arrange
        String name = "Test User";
        String email = "test@example.com";
        String plainPassword = "plainPassword123";
        String hashedPassword = "$2a$10$differentHashedPassword";

        when(bCryptPasswordEncoder.encode(plainPassword)).thenReturn(hashedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals(hashedPassword, user.getHashedPassword());
            return user;
        });

        // Act
        authService.signUpUser(name, email, plainPassword);

        // Assert
        verify(bCryptPasswordEncoder, times(1)).encode(plainPassword);
    }

    @Test
    void loginUser_ShouldGenerateJWTToken() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(bCryptPasswordEncoder.matches(password, testUser.getHashedPassword())).thenReturn(true);
        when(sessionRepository.findAllByUserIdAndSessionStatus(testUser.getId(), SessionStatus.ACTIVE))
            .thenReturn(new HashSet<>());
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<SessionDTO> result = authService.loginUser(email, password);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getBody());
        verify(sessionRepository, times(1)).save(argThat(session ->
            session.getToken() != null && !session.getToken().isEmpty()
        ));
    }
}
