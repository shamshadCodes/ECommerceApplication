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
        this.email = user.getEmail();
        this.name = user.getName();
        this.roles = user.getRoles();
    }
}
