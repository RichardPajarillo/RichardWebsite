package org.example.richardwebsite.repository;

import org.example.richardwebsite.model.Cart;
import org.example.richardwebsite.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUser_Id_shouldReturnCartWhenExists() {
        // Arrange
        User user = new User("username", "password", "USER");
        user = userRepository.save(user);
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        // Act
        Optional<Cart> result = cartRepository.findByUser_Id(user.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(cart, result.get());
    }

    @Test
    void findByUser_Id_shouldReturnEmptyWhenNotExists() {
        // Arrange
        // No cart saved

        // Act
        Optional<Cart> result = cartRepository.findByUser_Id(1L);

        // Assert
        assertFalse(result.isPresent());
    }
}