package org.example.richardwebsite.test.model;

import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.model.Cart;
import org.example.richardwebsite.model.CartItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    void fullConstructor_shouldSetFields() {
        Book book = new Book();
        Cart cart = new Cart();
        CartItem item = new CartItem(book, 3, cart);

        assertEquals(book, item.getBook());
        assertEquals(3, item.getQuantity());
        assertEquals(cart, item.getCart());
    }

    @Test
    void setters_shouldUpdateFields() {
        CartItem item = new CartItem();
        Book book = new Book();
        Cart cart = new Cart();

        item.setBook(book);
        item.setQuantity(10);
        item.setCart(cart);

        assertEquals(book, item.getBook());
        assertEquals(10, item.getQuantity());
        assertEquals(cart, item.getCart());
    }
}