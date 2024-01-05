package com.example.userManagement.dto;

import com.example.userManagement.model.Role;
import com.example.userManagement.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private String email;
    private String name;
    private Set<Role> roles;

    public UserDTO(User user) {
        this.email = email;
        this.name = name;
        this.roles = roles;
    }
}
