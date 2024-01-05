package com.example.userManagement.service;

import com.example.userManagement.dto.LoginRequestDTO;
import com.example.userManagement.dto.SignUpRequestDTO;
import com.example.userManagement.dto.UserDTO;
import com.example.userManagement.exception.InvalidLoginCredentialsException;
import com.example.userManagement.model.User;
import com.example.userManagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public UserDTO loginUser(LoginRequestDTO loginRequestDTO) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequestDTO.getEmail());

        if(userOptional.isEmpty()){
            throw new InvalidLoginCredentialsException("User does not exist");
        }

        User user = userOptional.get();

        if(!user.getPassword().equals(loginRequestDTO.getPassword())){
            throw new InvalidLoginCredentialsException("Invalid Credentials");
        }

        //TODO: Create session and send session token as cookie
        return new UserDTO(user);
    }
}
