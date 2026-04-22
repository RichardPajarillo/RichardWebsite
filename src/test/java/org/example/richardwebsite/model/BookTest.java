package org.example.richardwebsite.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void defaultConstructor_shouldCreateBook() {
        Book book = new Book();
        assertNotNull(book);
    }

    @Test
    void fullConstructor_shouldSetAllFields() {
        Book book = new Book("cover", "title", "author", "genre", "about", 10.0, 5);
        assertEquals("cover", book.getCover());
        assertEquals("title", book.getTitle());
        assertEquals("author", book.getAuthor());
        assertEquals("genre", book.getGenre());
        assertEquals("about", book.getAbout());
        assertEquals(10.0, book.getPrice());
        assertEquals(5, book.getQuantity());
    }

    @Test
    void setters_shouldUpdateFields() {
        Book book = new Book();
        book.setId(1L);
        book.setCover("cover");
        book.setTitle("title");
        book.setAuthor("author");
        book.setGenre("genre");
        book.setAbout("about");
        book.setPrice(10.0);
        book.setQuantity(5);

        assertEquals(1L, book.getId());
        assertEquals("cover", book.getCover());
        assertEquals("title", book.getTitle());
        assertEquals("author", book.getAuthor());
        assertEquals("genre", book.getGenre());
        assertEquals("about", book.getAbout());
        assertEquals(10.0, book.getPrice());
        assertEquals(5, book.getQuantity());
    }
}