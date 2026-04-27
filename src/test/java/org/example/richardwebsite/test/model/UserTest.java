package org.example.richardwebsite.test.model;

import org.example.richardwebsite.model.Cart;
import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    void settersAndGetters_shouldHandleAllFields() {
        User user = new User();

        // ID coverage
        user.setId(10L);
        assertEquals(10L, user.getId());

        // Role coverage
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());

        // Cart coverage
        Cart cart = new Cart();
        user.setCart(cart);
        assertEquals(cart, user.getCart());

        // Orders coverage
        List<Order> orders = new ArrayList<>();
        user.setOrders(orders);
        assertEquals(orders, user.getOrders());
    }

    @Test
    void setUsername_shouldHandleNullAndTrim() {
        User user = new User();

        // Branch: username != null (True) + Trim logic
        user.setUsername("  richard  ");
        assertEquals("richard", user.getUsername());

        // Branch: username != null (False)
        user.setUsername(null);
        assertNull(user.getUsername());
    }

    @Test
    void setPassword_shouldHandleNullAndTrim() {
        User user = new User();

        // Branch: password != null (True) + Trim logic
        user.setPassword("  secret123  ");
        assertEquals("secret123", user.getPassword());

        // Branch: password != null (False)
        user.setPassword(null);
        assertNull(user.getPassword());
    }
}