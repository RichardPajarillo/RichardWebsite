package org.example.richardwebsite.service;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.CartRepository;
import org.example.richardwebsite.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private CheckoutService checkoutService;

    @Test
    void checkout_shouldCreateOrderAndClearCart() {
        // Arrange
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(securityService.getCurrentUser()).thenReturn(user);

        Book book = new Book("cover", "title", "author", "genre", "about", 10.0, 5);
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setItems(Arrays.asList(cartItem));

        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        // Act
        checkoutService.checkout();

        // Assert
        verify(orderRepository).save(any(Order.class));
        verify(cartRepository).save(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void checkout_shouldDoNothingWhenCartIsEmpty() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(securityService.getCurrentUser()).thenReturn(user);

        Cart cart = new Cart();
        cart.setItems(Arrays.asList()); // empty

        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.of(cart));

        // Act
        checkoutService.checkout();

        // Assert
        verify(orderRepository, never()).save(any(Order.class));
        verify(cartRepository, never()).save(cart);
    }
}