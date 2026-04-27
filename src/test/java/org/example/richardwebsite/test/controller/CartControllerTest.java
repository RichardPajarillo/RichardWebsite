package org.example.richardwebsite.test.controller;

import org.example.richardwebsite.controller.CartController;
import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.*;
import org.example.richardwebsite.service.SecurityService;
import org.example.richardwebsite.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean // Or @MockBean depending on your Spring version
    private CartRepository cartRepository;

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private SecurityService securityService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private CartItemRepository cartItemRepository;


    private User mockUser;
    private Cart mockCart;

    @BeforeEach
    void setup() {
        // Re-initialize mockMvc with Spring Security applied
        this.mockMvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("user");

        when(securityService.getCurrentUser()).thenReturn(mockUser);

        mockCart = new Cart();
        mockCart.setId(10L);
        mockCart.setUser(mockUser);
        mockCart.setItems(new ArrayList<>());
    }


    @Test
    @WithMockUser
    void addToCart_shouldAddAndRedirect() throws Exception {
        Book book = new Book();
        book.setId(5L);
        when(bookRepository.findById(5L)).thenReturn(Optional.of(book));
        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.of(mockCart));

        mockMvc.perform(post("/cart/add/5")
                        .param("qty", "1")
                        .with(csrf())) // Critical for 302/403 errors
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Matches your mock user
    void viewCart_shouldCreateNewCart_WhenNoneExists() throws Exception {
        // Arrange
        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);
        when(cartItemRepository.findByCart_Id(eq(10L), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of()));

        // Act & Assert
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("cart", "itemPage", "items"));

        verify(cartRepository).save(any(Cart.class));
    }


    @Test
    @WithMockUser
    void addToCart_shouldAddBookAndRedirect() throws Exception {
        Book book = new Book();
        book.setId(5L);
        when(bookRepository.findById(5L)).thenReturn(Optional.of(book));
        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.of(mockCart));

        mockMvc.perform(post("/cart/add/5")
                        .param("qty", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartRepository).save(mockCart);
    }

    @Test
    @WithMockUser
    void remove_shouldRemoveItemAndRedirect() throws Exception {
        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.of(mockCart));

        mockMvc.perform(get("/cart/remove/5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartRepository).save(mockCart);
    }

    @Test
    @WithMockUser
    void updateQty_shouldUpdateItemQuantityAndReturnTotal() throws Exception {
        // Setup cart with one item
        Book book = new Book();
        book.setId(5L);
        book.setPrice(10.0);

        CartItem item = new CartItem();
        item.setBook(book);
        item.setQuantity(1);
        mockCart.getItems().add(item);

        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.of(mockCart));

        mockMvc.perform(post("/cart/update")
                        .param("bookId", "5")
                        .param("qty", "3")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("30.0")); // Assuming Cart.getTotal() calculates this

        verify(cartRepository).saveAndFlush(mockCart);
    }
}