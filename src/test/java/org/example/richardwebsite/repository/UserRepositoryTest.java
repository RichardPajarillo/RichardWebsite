package org.example.richardwebsite.repository;

import org.example.richardwebsite.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_shouldReturnUserWhenExists() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findByUsername("testuser");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
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