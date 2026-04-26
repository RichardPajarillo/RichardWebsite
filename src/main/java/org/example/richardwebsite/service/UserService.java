package org.example.richardwebsite.service;

import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        // 1. Check if the username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            // You can throw a custom exception here to catch in your Controller
            throw new RuntimeException("Username '" + user.getUsername() + "' is already taken.");
        }
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}