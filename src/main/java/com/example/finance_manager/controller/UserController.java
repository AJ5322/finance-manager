package com.example.finance_manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import com.example.finance_manager.dto.AuthUpdateResponse;
import com.example.finance_manager.dto.UpdateUserRequest;
import com.example.finance_manager.dto.UserResponse;
import com.example.finance_manager.entity.User;
import com.example.finance_manager.security.CurrentUserService;
import com.example.finance_manager.security.JwtService;
import com.example.finance_manager.service.UserService;

@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;
    private final JwtService jwtService;

    public UserController(
            UserService userService,
            PasswordEncoder passwordEncoder,
            CurrentUserService currentUserService,
            JwtService jwtService) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.currentUserService = currentUserService;
        this.jwtService = jwtService;
    }

    @PostMapping("/users")
    public UserResponse createUser(@Valid @RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userService.saveUser(user);

        return convertToUserResponse(savedUser);
    }

    @GetMapping("/users")
    public UserResponse getCurrentUser() {

        User currentUser = currentUserService.getCurrentUser();

        return convertToUserResponse(currentUser);
    }

    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable Long id) {

        User currentUser = currentUserService.getCurrentUser();

        checkOwnership(id, currentUser);

        return convertToUserResponse(currentUser);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {

        User currentUser = currentUserService.getCurrentUser();

        checkOwnership(id, currentUser);

        userService.deleteUser(id);
    }

    @PutMapping("/users/{id}")
    public AuthUpdateResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        checkOwnership(id, currentUser);

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // Keep the existing encrypted password
        user.setPassword(currentUser.getPassword());

        User updatedUser = userService.updateUser(id, user);

        // Generate a new JWT using the updated email
        String newToken = jwtService.generateToken(updatedUser.getEmail());

        UserResponse userResponse = convertToUserResponse(updatedUser);

        return new AuthUpdateResponse(userResponse, newToken);
    }

    private void checkOwnership(Long requestedUserId, User currentUser) {

        if (!currentUser.getId().equals(requestedUserId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot access another user's profile");
        }
    }

    private UserResponse convertToUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }
}