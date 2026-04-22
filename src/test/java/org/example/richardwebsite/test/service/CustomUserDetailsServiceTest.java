package org.example.richardwebsite.test.service;

import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.UserRepository;
import org.example.richardwebsite.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetailsWhenUserExists() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole("USER");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(username);

        // Assert
        assertEquals(username, result.getUsername());
        assertEquals("password", result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_shouldThrowExceptionWhenUserNotExists() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }
}