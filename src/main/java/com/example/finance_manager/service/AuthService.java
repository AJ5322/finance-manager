package com.example.finance_manager.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.finance_manager.dto.AuthRequest;
import com.example.finance_manager.dto.AuthResponse;
import com.example.finance_manager.entity.User;
import com.example.finance_manager.security.JwtService;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest request) {

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}