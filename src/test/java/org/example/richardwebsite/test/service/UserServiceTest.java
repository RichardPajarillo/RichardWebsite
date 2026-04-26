package org.example.richardwebsite.test.service;

import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.UserRepository;
import org.example.richardwebsite.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void save_shouldReturnSavedUser() {
        // Arrange
        User user = new User();
        user.setUsername("newuser");
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.save(user);

        // Assert
        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void save_shouldThrowExceptionWhenUsernameExists() {
        // This test covers the 2nd branch (if statement is true)
        // Arrange
        User user = new User();
        user.setUsername("existingUser");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.save(user);
        });

        assertEquals("Username 'existingUser' is already taken.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // Ensures save was NOT called
    }

    @Test
    void findByUsername_shouldReturnUserWhenExists() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        // Mocking the repository to return an Optional containing our user
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findByUsername(username);

        // Assert
        // 1. Verify the Optional is not empty
        assertTrue(result.isPresent(), "Optional should contain a user");

        // 2. Compare the object INSIDE the Optional to our expected user
        assertEquals(user, result.get(), "The user inside the Optional should match");

        // 3. (Optional) Double check the data
        assertEquals(username, result.get().getUsername());
    }

    @Test
    void findByUsername_shouldReturnEmptyWhenNotExists() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByUsername(username);

        // Assert
        // Option 1: The most accurate check for Optionals
        assertTrue(result.isEmpty(), "The result should be an empty Optional");

        // Option 2: Comparing against the Optional.empty() constant
        assertEquals(Optional.empty(), result);
    }
}