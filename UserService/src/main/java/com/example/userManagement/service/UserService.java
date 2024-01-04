package com.example.userManagement.service;

import com.example.userManagement.exception.UserNotFoundException;
import com.example.userManagement.model.Role;
import com.example.userManagement.model.User;
import com.example.userManagement.repository.RoleRepository;
import com.example.userManagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User getUserDetails(UUID userId){
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()){
            throw new UserNotFoundException("Invalid User");
        }

        return userOptional.get();
    }

    public User setUserRoles(UUID userId, List<UUID> roleIds){
        Optional<User> userOptional = userRepository.findById(userId);
        Set<Role> roles = roleRepository.findRolesByIdIn(roleIds);

        if(userOptional.isEmpty()){
            throw new UserNotFoundException("Invalid User");
        }

        User user = userOptional.get();
        user.setRoles(roles);

        return user;

    }
}
