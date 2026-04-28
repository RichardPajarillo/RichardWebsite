package org.example.richardwebsite.test.service;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.OrderRepository;
import org.example.richardwebsite.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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

        Book book = new Book("cover", "title", "author", "genre", "about", BigDecimal.TEN, 5);
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

    @Test
    void createOrder_shouldStartAtOne_WhenNoPreviousOrdersExist() {
        // Arrange: Mock findMaxUserOrderNumber to return null
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        Cart cart = new Cart(); // Empty cart is fine for this branch

        when(orderRepository.findMaxUserOrderNumber(1L)).thenReturn(null);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Order result = orderService.createOrder(user, cart);

        // Assert: Verify it defaults to 1
        assertEquals(1, result.getUserOrderNumber());
    }

    @Test
    void calculateTotal_shouldReturnZero_WhenCartOrItemsAreNull() {
        // Arrange
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUsername()).thenReturn("testUser"); // Avoid potential issues with customerName

        // Testing Branch: cart.getItems() == null
        Cart cartWithNullItems = new Cart();
        cartWithNullItems.setItems(null);

        // Mock dependencies for the method to complete
        when(orderRepository.findMaxUserOrderNumber(1L)).thenReturn(0);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Order result = orderService.createOrder(user, cartWithNullItems);

        // Assert
        assertEquals(java.math.BigDecimal.ZERO, result.getTotal());
        // Verify that no items were added to the order
        assertTrue(result.getItems() == null || result.getItems().isEmpty());
    }

    @Test
    void createOrder_shouldHandleNullCart() {
        // Arrange
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUsername()).thenReturn("testUser");

        // Mock max order number and save
        when(orderRepository.findMaxUserOrderNumber(1L)).thenReturn(0);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act: Pass a strictly NULL cart
        Order result = orderService.createOrder(user, null);

        // Assert: Branches for (cart == null) and (cart != null) are covered
        assertEquals(java.math.BigDecimal.ZERO, result.getTotal());
        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_shouldHandleEmptyCartItems() {
        // Arrange
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUsername()).thenReturn("testUser");

        // Cart with an EMPTY list (not null)
        Cart emptyCart = new Cart();
        emptyCart.setItems(java.util.Collections.emptyList());

        when(orderRepository.findMaxUserOrderNumber(1L)).thenReturn(0);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Order result = orderService.createOrder(user, emptyCart);

        // Assert: Branches for (getItems() != null) are true, but loop is skipped
        assertEquals(java.math.BigDecimal.ZERO, result.getTotal());
        assertTrue(result.getItems() == null || result.getItems().isEmpty());
    }
}