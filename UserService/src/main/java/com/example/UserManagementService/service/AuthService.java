package com.example.UserManagementService.service;

import com.example.UserManagementService.dto.SessionDTO;
import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.exception.InvalidLoginCredentialsException;
import com.example.UserManagementService.exception.SessionNotFoundException;
import com.example.UserManagementService.model.Session;
import com.example.UserManagementService.model.SessionStatus;
import com.example.UserManagementService.model.User;
import com.example.UserManagementService.repository.SessionRepository;
import com.example.UserManagementService.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDTO signUpUser(String name, String email, String password) {
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);

        return new UserDTO(savedUser);
    }

    public ResponseEntity<UserDTO> loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()){
            throw new InvalidLoginCredentialsException("User does not exist");
        }

        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new InvalidLoginCredentialsException("Invalid Credentials");
        }

        //Generating token
        MacAlgorithm macAlgorithm = Jwts.SIG.HS256;
        SecretKey key = macAlgorithm.key().build();

        long expiryDateInMilliseconds = LocalDate.now().plusDays(3).toEpochDay()*24*60*60*1000;

        //Adding Claims
        Map<String, Object> jsonForJWT = new HashMap<>();
        jsonForJWT.put("email", user.getEmail());
        jsonForJWT.put("roles", user.getRoles());
        jsonForJWT.put("createdAt", new Date());
        jsonForJWT.put("expiryAt", new Date(expiryDateInMilliseconds));

        String token = Jwts.builder()
                .claims(jsonForJWT)
                .signWith(key, macAlgorithm)
                .compact();

        //Creating a session
        Session session = new Session();
        session.setUser(user);
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setIssuedAt(new Date());
        session.setExpiringAt(new Date(expiryDateInMilliseconds));

        sessionRepository.save(session);

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, token);

        return new ResponseEntity<>(new UserDTO(user), headers, HttpStatus.OK);
    }

    public void logoutUser(UUID userID, String token) {
        Optional<Session> optionalSession = sessionRepository.findByTokenAndUserId(token, userID);

        if(optionalSession.isEmpty()){
            throw new SessionNotFoundException("Session not found");
        }

        Session session = optionalSession.get();

        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
    }

    public SessionDTO validateSession(String token, UUID userID) {
        Optional<Session> optionalSession = sessionRepository.findByTokenAndUserId(token, userID);
        if(optionalSession.isEmpty()){
            throw new SessionNotFoundException("Session not found");
        }

        Session session = optionalSession.get();

        return new SessionDTO(session);
    }
}
