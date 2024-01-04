package com.example.userManagement.service;

import com.example.userManagement.exception.UserNotFoundException;
import com.example.userManagement.model.Role;
import com.example.userManagement.model.User;
import com.example.userManagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserDetails(UUID userId){
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()){
            throw new UserNotFoundException("Invalid User");
        }

        return userOptional.get();
    }

    public List<Role> setUserRoles(UUID userId, List<UUID> roleIds){
        return null;
    }
}
