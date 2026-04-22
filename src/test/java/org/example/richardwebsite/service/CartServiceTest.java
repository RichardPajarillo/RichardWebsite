package org.example.richardwebsite.service;

import org.example.richardwebsite.model.Cart;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private CartService cartService;

    @Test
    void getCart_shouldReturnExistingCart() {
        // Arrange
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        Cart cart = new Cart();
        when(securityService.getCurrentUser()).thenReturn(user);
        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.of(cart));

        // Act
        Cart result = cartService.getCart();

        // Assert
        assertEquals(cart, result);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void getCart_shouldCreateNewCartWhenNotExists() {
        // Arrange
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(securityService.getCurrentUser()).thenReturn(user);
        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.empty());
        Cart newCart = new Cart();
        newCart.setUser(user);
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        // Act
        Cart result = cartService.getCart();

        // Assert
        assertEquals(newCart, result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void save_shouldCallRepositorySave() {
        // Arrange
        Cart cart = new Cart();

        // Act
        Cart result = cartService.save(cart);

        // Assert
        assertEquals(cart, result);
        verify(cartRepository).save(cart);
    }
}