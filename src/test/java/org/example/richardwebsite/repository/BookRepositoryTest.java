package org.example.richardwebsite.repository;

import org.example.richardwebsite.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findAll_shouldReturnPagedBooks() {
        // Arrange
        Book book1 = new Book("cover1", "Title1", "Author1", "Genre1", "About1", 10.0, 5);
        Book book2 = new Book("cover2", "Title2", "Author2", "Genre2", "About2", 20.0, 3);
        bookRepository.save(book1);
        bookRepository.save(book2);

        // Act
        Page<Book> result = bookRepository.findAll(PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().contains(book1));
        assertTrue(result.getContent().contains(book2));
    }

    @Test
    void findByTitleContainingIgnoreCase_shouldReturnMatchingBooks() {
        // Arrange
        Book book1 = new Book("cover1", "Java Book", "Author1", "Genre1", "About1", 10.0, 5);
        Book book2 = new Book("cover2", "Python Book", "Author2", "Genre2", "About2", 20.0, 3);
        bookRepository.save(book1);
        bookRepository.save(book2);

        // Act
        Page<Book> result = bookRepository.findByTitleContainingIgnoreCase("java", PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(book1, result.getContent().get(0));
    }

    @Test
    void findByGenreIgnoreCase_shouldReturnMatchingBooks() {
        // Arrange
        Book book1 = new Book("cover1", "Title1", "Author1", "Fiction", "About1", 10.0, 5);
        Book book2 = new Book("cover2", "Title2", "Author2", "Non-Fiction", "About2", 20.0, 3);
        bookRepository.save(book1);
        bookRepository.save(book2);

        // Act
        Page<Book> result = bookRepository.findByGenreIgnoreCase("fiction", PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(book1, result.getContent().get(0));
    }
}