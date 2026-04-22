package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void viewBooks_shouldReturnIndexViewWithBooks() throws Exception {
        // Arrange
        List<Book> books = Arrays.asList(new Book(), new Book());
        Page<Book> booksPage = new PageImpl<>(books, PageRequest.of(0, 8), books.size());
        when(bookRepository.findAll(any(PageRequest.class))).thenReturn(booksPage);

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("listBooks"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1));
    }

    @Test
    void viewBooks_withSearch_shouldReturnFilteredBooks() throws Exception {
        // Arrange
        String search = "test";
        List<Book> books = Arrays.asList(new Book());
        Page<Book> booksPage = new PageImpl<>(books, PageRequest.of(0, 8), books.size());
        when(bookRepository.findByTitleContainingIgnoreCase(eq(search), any(PageRequest.class))).thenReturn(booksPage);

        // Act & Assert
        mockMvc.perform(get("/").param("search", search))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("search", search));
    }

    @Test
    void showNewBookForm_shouldReturnNewBookView() throws Exception {
        mockMvc.perform(get("/showNewBookForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("new_book"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void showFormForUpdate_shouldReturnUpdateBookViewWhenBookExists() throws Exception {
        // Arrange
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        when(bookRepository.findById(id)).thenReturn(java.util.Optional.of(book));

        // Act & Assert
        mockMvc.perform(get("/showFormForUpdate/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("update_book"))
                .andExpect(model().attribute("book", book));
    }

    @Test
    void showFormForUpdate_shouldThrowExceptionWhenBookNotExists() throws Exception {
        // Arrange
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/showFormForUpdate/{id}", id))
                .andExpect(status().is5xxServerError()); // Since it throws IllegalArgumentException
    }
}