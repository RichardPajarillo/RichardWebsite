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
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertEquals(user, result);
    }

    @Test
    void findByUsername_shouldReturnNullWhenNotExists() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertNull(result);
    }
}