package org.example.richardwebsite.service;

import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        // Arrange
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<Book> result = bookService.getAllBooks();

        // Assert
        assertEquals(books, result);
        verify(bookRepository).findAll();
    }

    @Test
    void getBookById_shouldReturnBookWhenExists() {
        // Arrange
        Long id = 1L;
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // Act
        Optional<Book> result = bookService.getBookById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(book, result.get());
        verify(bookRepository).findById(id);
    }

    @Test
    void getBookById_shouldReturnEmptyWhenNotExists() {
        // Arrange
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookService.getBookById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(bookRepository).findById(id);
    }

    @Test
    void saveBook_shouldCallRepositorySave() {
        // Arrange
        Book book = new Book();

        // Act
        bookService.saveBook(book);

        // Assert
        verify(bookRepository).save(book);
    }

    @Test
    void deleteBook_shouldCallRepositoryDeleteById() {
        // Arrange
        Long id = 1L;

        // Act
        bookService.deleteBook(id);

        // Assert
        verify(bookRepository).deleteById(id);
    }
}