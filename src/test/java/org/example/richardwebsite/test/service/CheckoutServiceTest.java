package org.example.richardwebsite.test.service;

import jakarta.transaction.Transactional;
import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.CartRepository;
import org.example.richardwebsite.repository.OrderRepository;
import org.example.richardwebsite.service.CheckoutService;
import org.example.richardwebsite.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
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
        // 1. Arrange User and Security Mocks
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(securityService.getCurrentUser()).thenReturn(user);

        // 2. Define the Book
        Book book = new Book("cover", "title", "author", "genre", "about", 10.0, 5);

        // 3. Define the CartItem (This must come BEFORE creating the list)
        CartItem cartItem = new CartItem(); // This defines the symbol 'cartItem'
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        // 4. Initialize the Cart with a MUTABLE ArrayList
        Cart cart = new Cart();
        // Use the variable 'cartItem' defined above
        cart.setItems(new ArrayList<>(Arrays.asList(cartItem)));

        // 5. Setup Repository Mocks
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
        User user = mock(User.class); // Use a mock instead of "new User()"
        when(user.getId()).thenReturn(1L);
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