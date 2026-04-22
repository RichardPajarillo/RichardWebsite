package org.example.richardwebsite.service;

import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityService securityService;

    @Test
    void getCurrentUser_shouldReturnUser() {
        // Arrange
        String username = "testuser";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        try (MockedStatic<SecurityContextHolder> mockedHolder = mockStatic(SecurityContextHolder.class)) {
            mockedHolder.when(SecurityContextHolder::getContext).thenReturn(context);

            // Act
            User result = securityService.getCurrentUser();

            // Assert
            assertEquals(user, result);
        }
    }

    @Test
    void isAdmin_shouldReturnTrueWhenRoleIsAdmin() {
        // Arrange
        User user = new User();
        user.setRole("ROLE_ADMIN");

        try (MockedStatic<SecurityContextHolder> mockedHolder = mockStatic(SecurityContextHolder.class)) {
            // Mock getCurrentUser to return admin user
            SecurityService spyService = spy(securityService);
            doReturn(user).when(spyService).getCurrentUser();

            // Act
            boolean result = spyService.isAdmin();

            // Assert
            assertTrue(result);
        }
    }

    @Test
    void isAdmin_shouldReturnFalseWhenRoleIsNotAdmin() {
        // Arrange
        User user = new User();
        user.setRole("ROLE_USER");

        SecurityService spyService = spy(securityService);
        doReturn(user).when(spyService).getCurrentUser();

        // Act
        boolean result = spyService.isAdmin();

        // Assert
        assertFalse(result);
    }
}