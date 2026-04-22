package org.example.richardwebsite.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CustomAccessDeniedHandlerTest {

    private final CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();

    @Test
    void handle_shouldRedirectToLoginWithError() throws IOException {
        // Arrange
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        AccessDeniedException ex = new AccessDeniedException("Access denied");

        // Act
        handler.handle(request, response, ex);

        // Assert
        MockHttpServletResponse mockResponse = (MockHttpServletResponse) response;
        assertEquals("/login?error=restricted", mockResponse.getRedirectedUrl());
    }
}