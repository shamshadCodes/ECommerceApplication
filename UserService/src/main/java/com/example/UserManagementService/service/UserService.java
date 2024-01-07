package com.example.UserManagementService.service;

import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.exception.UserNotFoundException;
import com.example.UserManagementService.model.Role;
import com.example.UserManagementService.model.User;
import com.example.UserManagementService.repository.RoleRepository;
import com.example.UserManagementService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDTO getUserDetails(UUID userId){
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()){
            throw new UserNotFoundException("User does not exist");
        }
        User user = userOptional.get();

        return new UserDTO(user);
    }

    public UserDTO setUserRoles(UUID userId, List<UUID> roleIds){
        Optional<User> userOptional = userRepository.findById(userId);
        Set<Role> roles = roleRepository.findRolesByIdIn(roleIds);

        if(userOptional.isEmpty()){
            throw new UserNotFoundException("Invalid User");
        }

        User user = userOptional.get();
        user.setRoles(roles);

        return new UserDTO(user);

    }
}
