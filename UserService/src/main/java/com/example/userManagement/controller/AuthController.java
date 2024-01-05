package com.example.userManagement.controller;

import com.example.userManagement.dto.LoginRequestDTO;
import com.example.userManagement.dto.SignUpRequestDTO;
import com.example.userManagement.dto.UserDTO;
import com.example.userManagement.model.User;
import com.example.userManagement.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        UserDTO userDTO = authService.signUpUser(signUpRequestDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        UserDTO userDTO = authService.loginUser(loginRequestDTO);
        return ResponseEntity.ok(userDTO);
    }
}
