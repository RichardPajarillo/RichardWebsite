package org.example.richardwebsite.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void defaultConstructor_shouldCreateUser() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void fullConstructor_shouldSetAllFields() {
        User user = new User("username", "password", "USER");
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("USER", user.getRole());
    }

    @Test
    void setters_shouldUpdateFields() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole("ADMIN");

        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("ADMIN", user.getRole());
    }
}