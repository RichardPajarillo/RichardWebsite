package org.example.richardwebsite.test.repository;

import jakarta.transaction.Transactional;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_shouldReturnUserWhenExists() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password"); // Add this
        user.setRole("USER");         // Add this if role is also non-nullable
        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findByUsername("testuser");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void findByUsername_shouldReturnEmptyWhenNotExists() {
        // Arrange
        // No user saved

        // Act
        Optional<User> result = userRepository.findByUsername("testuser");

        // Assert
        assertFalse(result.isPresent());
    }
}