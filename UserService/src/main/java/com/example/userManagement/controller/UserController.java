package com.example.userManagement.controller;

import com.example.userManagement.dto.SetUserRolesRequestDTO;
import com.example.userManagement.model.Role;
import com.example.userManagement.model.User;
import com.example.userManagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserDetails(@PathVariable("id") UUID userId){
        User user = userService.getUserDetails(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<User> setRoles(@PathVariable("id") UUID userId, @RequestBody SetUserRolesRequestDTO request){
        User user = userService.setUserRoles(userId, request.getRoleIds());
        return ResponseEntity.ok(user);
    }
}
