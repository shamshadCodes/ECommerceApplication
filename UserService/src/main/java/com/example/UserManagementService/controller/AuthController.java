package com.example.UserManagementService.controller;

import com.example.UserManagementService.dto.*;
import com.example.UserManagementService.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        UserDTO userDTO = authService.signUpUser(signUpRequestDTO.getName(), signUpRequestDTO.getEmail(), signUpRequestDTO.getPassword());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        return authService.loginUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
    }

    @PutMapping("/logout/{id}")
    public ResponseEntity<Void> logout (@PathVariable UUID id, @RequestHeader String token){
        authService.logoutUser(id, token);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<SessionDTO> validateSession(@RequestBody validateSessionRequestDTO validateSessionRequestDTO){
        SessionDTO sessionDTO = authService.validateSession(validateSessionRequestDTO.getToken(), validateSessionRequestDTO.getUserID());
        return new ResponseEntity<>(sessionDTO, HttpStatus.OK);
    }

}
