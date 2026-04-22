package org.example.richardwebsite.service;

import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.UserRepository;
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
        User savedUser = new User();
        when(userRepository.save(user)).thenReturn(savedUser);

        // Act
        User result = userService.save(user);

        // Assert
        assertEquals(savedUser, result);
        verify(userRepository).save(user);
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
        verify(userRepository).findByUsername(username);
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
        verify(userRepository).findByUsername(username);
    }
}