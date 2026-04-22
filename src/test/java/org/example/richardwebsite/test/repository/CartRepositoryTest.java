package org.example.richardwebsite.test.repository;

import jakarta.transaction.Transactional;
import org.example.richardwebsite.model.Cart;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.CartRepository;
import org.example.richardwebsite.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional // Added: This cleans the DB after every test
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUser_Id_shouldReturnCartWhenExists() {
        // Arrange - Use unique names to be safe
        User user = new User("cartUser", "password", "USER");
        user = userRepository.save(user);
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        // Act
        Optional<Cart> result = cartRepository.findByUser_Id(user.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getUser().getId());
    }

    @Test
    void findByUser_Id_shouldReturnEmptyWhenNotExists() {
        // Arrange
        // Create a user but DON'S save a cart for them
        User user = new User("noCartUser", "password", "USER");
        user = userRepository.save(user);

        // Act - Query specifically for THIS user's ID
        Optional<Cart> result = cartRepository.findByUser_Id(user.getId());

        // Assert
        assertFalse(result.isPresent(), "Should be empty for a user with no cart");
    }
}