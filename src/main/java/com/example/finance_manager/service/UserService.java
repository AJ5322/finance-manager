package com.example.finance_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.finance_manager.entity.User;
import com.example.finance_manager.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already registered");
        }

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User updatedUser) {

        User existingUser =
                userRepository.findById(id).orElseThrow();

        Optional<User> userWithSameEmail =
                userRepository.findByEmail(updatedUser.getEmail());

        if (userWithSameEmail.isPresent()
                && !userWithSameEmail.get().getId().equals(id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already registered");
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setPhone(updatedUser.getPhone());

        return userRepository.save(existingUser);
    }

    public List<User> getAllUsersById(Long userId) {
        return userRepository.findAll();
    }
}