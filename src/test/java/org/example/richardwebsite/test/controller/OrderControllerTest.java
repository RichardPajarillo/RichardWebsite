package org.example.richardwebsite.test.controller;


import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.OrderRepository;
import org.example.richardwebsite.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private SecurityService securityService;

    private User currentUser;

    @BeforeEach
    void setUp() {
        // Set up a mock user with a specific ID using reflection
        currentUser = new User();
        ReflectionTestUtils.setField(currentUser, "id", 1L);
        when(securityService.getCurrentUser()).thenReturn(currentUser);
    }

    @Test
    @WithMockUser
    void orders_shouldReturnOrdersView() throws Exception {
        // 1. Setup mock data
        Order order = new Order();
        List<Order> userOrders = List.of(order);

        // 2. Create a Mock Page object
        // PageImpl is the standard Spring implementation of Page
        Page<Order> mockPage = new PageImpl<>(userOrders);

        // 3. Update the Mockito 'when' to match the Pageable parameter
        // We use any(Pageable.class) to match whatever pagination the controller sends
        when(orderRepository.findByUser_Id(eq(currentUser.getId()), any(Pageable.class)))
                .thenReturn(mockPage);

        // 4. Act & Assert
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                // Check for the new orderPage attribute
                .andExpect(model().attributeExists("orderPage"))
                // Check that orders contains our list
                .andExpect(model().attribute("orders", userOrders));
    }

    @Test
    @WithMockUser
    void orderDetails_shouldReturnDetailsView_WhenUserOwnsOrder() throws Exception {
        // Arrange
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", 10L);
        order.setUser(currentUser); // Match the current user ID

        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

        // Act & Assert
        mockMvc.perform(get("/orders/{id}", 10L)
                        .with(csrf())) // 👈 Added
                .andExpect(status().isOk())
                .andExpect(view().name("order-details"))
                .andExpect(model().attribute("order", order));
    }

    @Test
    @WithMockUser
    void orders_shouldReturnOrdersViewWithUserOrders() throws Exception {
        // Arrange
        Order order = new Order();
        Page<Order> orderPage = new PageImpl<>(List.of(order));

        // Match the call: findByUser_Id(Long, Pageable)
        when(orderRepository.findByUser_Id(eq(1L), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(orderPage);

        // Act & Assert
        mockMvc.perform(get("/orders")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orderPage"))
                .andExpect(model().attribute("orders", orderPage.getContent()));
    }
}