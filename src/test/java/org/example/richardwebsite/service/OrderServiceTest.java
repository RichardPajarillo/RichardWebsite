package org.example.richardwebsite.service;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_shouldCreateAndSaveOrder() {
        // Arrange
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        Book book = new Book("cover", "title", "author", "genre", "about", 10.0, 5);
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setItems(Arrays.asList(cartItem));

        when(orderRepository.findMaxUserOrderNumber(1L)).thenReturn(5);
        Order savedOrder = new Order();
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Order result = orderService.createOrder(user, cart);

        // Assert
        assertEquals(savedOrder, result);
        verify(orderRepository).save(any(Order.class));
        // Check that the order has correct userOrderNumber, status, total, etc.
        // Since it's mocked, we can verify the save was called with correct order
    }
}