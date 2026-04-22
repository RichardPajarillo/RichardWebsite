package org.example.richardwebsite.test.model;

import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.model.Cart;
import org.example.richardwebsite.model.CartItem;
import org.example.richardwebsite.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private Book testBook;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setPrice(20.0);
    }

    @Test
    void defaultConstructor_shouldCreateEmptyCart() {
        Cart newCart = new Cart();
        assertNotNull(newCart);
        assertNotNull(newCart.getItems());
        assertTrue(newCart.getItems().isEmpty());
    }

    @Test
    void setUser_shouldSetUser() {
        User user = new User();
        user.setUsername("richard");
        cart.setUser(user);

        assertEquals("richard", cart.getUser().getUsername());
    }

    @Test
    void addItem_shouldAddNewItem() {
        cart.addItem(testBook, 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
        assertEquals("Test Book", cart.getItems().get(0).getBook().getTitle());
    }

    @Test
    void addItem_shouldIncreaseQuantityIfBookAlreadyInCart() {
        cart.addItem(testBook, 2);
        cart.addItem(testBook, 3); // Adding the same book again

        assertEquals(1, cart.getItems().size(), "Should not add a new row for the same book");
        assertEquals(5, cart.getItems().get(0).getQuantity(), "Quantity should be 2 + 3 = 5");
    }

    @Test
    void removeItem_shouldRemoveItemByBookId() {
        cart.addItem(testBook, 1);
        assertFalse(cart.getItems().isEmpty());

        cart.removeItem(1L); // Using the ID we set in setUp()

        assertTrue(cart.getItems().isEmpty(), "Cart should be empty after removing the only item");
    }

    @Test
    void getItems_shouldReturnEmptyListInitially() {
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
    }
}