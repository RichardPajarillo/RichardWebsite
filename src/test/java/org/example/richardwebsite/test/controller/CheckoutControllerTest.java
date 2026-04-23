package org.example.richardwebsite.test.controller;


import org.example.richardwebsite.model.Cart;
import org.example.richardwebsite.model.CartItem;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.CartRepository;
import org.example.richardwebsite.service.OrderService;
import org.example.richardwebsite.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils; // Added for ID injection
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SecurityService securityService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private CartRepository cartRepository;

    private User mockUser;
    private Cart mockCart;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        // Use reflection to set the private ID field to avoid using a public setter
        ReflectionTestUtils.setField(mockUser, "id", 1L);

        mockCart = new Cart();
        mockCart.setUser(mockUser);
        mockCart.setItems(new ArrayList<>());

        when(securityService.getCurrentUser()).thenReturn(mockUser);
        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.of(mockCart));
    }

    @Test
    @WithMockUser
    void showCheckout_shouldReturnCheckoutViewWithCart() throws Exception {
        mockMvc.perform(get("/checkout")
                        .with(csrf())) // 👈 Adds the mock CSRF object that Thymeleaf expects
                .andExpect(status().isOk())
                .andExpect(view().name("checkout"))
                .andExpect(model().attribute("cart", mockCart));
    }

    @Test
    @WithMockUser(roles = "USER")
    void checkout_shouldRedirectToOrders_WhenCartHasItems() throws Exception {
        // Arrange: Add an item to the cart
        mockCart.getItems().add(new CartItem());

        // Act & Assert
        mockMvc.perform(post("/checkout")
                        .with(csrf())
                        .with(user("testUser").roles("USER"))) // Explicitly inject user into the request
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        // Verify order creation
        verify(orderService, times(1)).createOrder(any(User.class), any(Cart.class));
    }

    @Test
    @WithMockUser
    void checkout_shouldRedirectToCart_WhenCartIsEmpty() throws Exception {
        // Ensure items list is empty to trigger the redirect
        mockCart.setItems(new ArrayList<>());

        mockMvc.perform(post("/checkout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart?error=empty"));

        verify(orderService, never()).createOrder(any(), any());
    }
}