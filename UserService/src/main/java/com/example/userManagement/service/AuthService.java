package com.example.userManagement.service;

import com.example.userManagement.dto.SignUpRequestDTO;
import com.example.userManagement.dto.UserDTO;
import com.example.userManagement.model.User;
import com.example.userManagement.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO signUpUser(SignUpRequestDTO signUpRequestDTO) {
        User user = new User();
        user.setName(signUpRequestDTO.getName());
        user.setEmail(signUpRequestDTO.getEmail());
        user.setPassword(signUpRequestDTO.getPassword());

        User savedUser = userRepository.save(user);

        return new UserDTO(savedUser);
    }
}
