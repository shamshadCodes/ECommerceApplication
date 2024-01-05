package com.example.userManagement.controller;

import com.example.userManagement.dto.SetUserRolesRequestDTO;
import com.example.userManagement.dto.UserDTO;
import com.example.userManagement.model.User;
import com.example.userManagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable("id") UUID userId){
        UserDTO userDTO = userService.getUserDetails(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<UserDTO> setRoles(@PathVariable("id") UUID userId, @RequestBody SetUserRolesRequestDTO request){
        UserDTO userDTO = userService.setUserRoles(userId, request.getRoleIds());
        return ResponseEntity.ok(userDTO);
    }
}
