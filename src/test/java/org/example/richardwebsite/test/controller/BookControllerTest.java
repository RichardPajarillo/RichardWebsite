package org.example.richardwebsite.test.controller;

import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disable security to focus on logic branches
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRepository bookRepository;

    // --- BRANCHES IN viewBooks() ---

    @Test
    @WithMockUser
    void viewBooks_shouldUseSearchBranch_WhenSearchProvided() throws Exception {
        Page<Book> page = new PageImpl<>(List.of(new Book()));
        when(bookRepository.findByTitleContainingIgnoreCase(eq("Java"), any())).thenReturn(page);

        mockMvc.perform(get("/")
                        .param("search", "Java")
                        .with(csrf())) // 👈 Add this even for GET if your template requires CSRF
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser // 1. Simulates a logged-in user
    void viewBooks_shouldUseGenreBranch_WhenGenreProvided() throws Exception {
        // Arrange
        Page<Book> page = new PageImpl<>(List.of(new Book()));
        when(bookRepository.findByGenreIgnoreCase(eq("Fiction"), any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/")
                        .param("genre", "Fiction")
                        .with(csrf())) // 2. Provides the CSRF object Thymeleaf is looking for
                .andExpect(status().isOk())
                .andExpect(model().attribute("genre", "Fiction"))
                .andExpect(view().name("index"));
    }

    // --- BRANCHES IN saveBook() ---

    @Test
    void saveBook_shouldReturnNewBookView_WhenValidationFailsAndIdIsNull() throws Exception {
        // Sending a Book with no fields triggers @NotBlank validation errors
        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", new Book()))
                .andExpect(status().isOk())
                .andExpect(view().name("new_book")) // Branch 1: result.hasErrors() && id == null
                .andExpect(model().attributeExists("priceError"));
    }

    @Test
    void saveBook_shouldReturnUpdateBookView_WhenValidationFailsAndIdExists() throws Exception {
        Book bookWithId = new Book();
        bookWithId.setId(99L); // ID exists

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", bookWithId))
                .andExpect(status().isOk())
                .andExpect(view().name("update_book")) // Branch 2: result.hasErrors() && id != null
                .andExpect(model().attributeExists("priceError"));
    }

    @Test
    void saveBook_shouldRedirectToNewForm_WhenCreationSuccessful() throws Exception {
        Book newBook = new Book("url", "Title", "Author", "Genre", "About", 10.0, 1);
        // id is null

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", newBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/showNewBookForm")) // Branch 3: success && !isUpdate
                .andExpect(flash().attribute("message", "Book saved successfully!"));
    }

    @Test
    void saveBook_shouldRedirectToUpdateForm_WhenUpdateSuccessful() throws Exception {
        Book existingBook = new Book("url", "Title", "Author", "Genre", "About", 10.0, 1);
        existingBook.setId(55L);

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", existingBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/showFormForUpdate/55")) // Branch 4: success && isUpdate
                .andExpect(flash().attribute("message", "Book updated successfully!"));
    }

    // --- ADDITIONAL BRANCHES FOR viewBooks() ---

    @Test
    @WithMockUser
    void viewBooks_shouldUseDefaultBranch_WhenNoParamsProvided() throws Exception {
        // Arrange: Mock the findAll branch
        Page<Book> page = new PageImpl<>(List.of(new Book()));
        when(bookRepository.findAll(any(PageRequest.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("listBooks"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser
    void viewBooks_shouldHandleNegativePageNumber() throws Exception {
        // Arrange: Testing the logic: (page == null || page < 0) ? 0 : page;
        Page<Book> page = new PageImpl<>(List.of(new Book()));
        when(bookRepository.findAll(PageRequest.of(0, 8))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/")
                        .param("page", "-5")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentPage", 0));
    }

    // --- BRANCHES FOR showNewBookForm & showFormForUpdate ---

    @Test
    @WithMockUser
    void showNewBookForm_shouldReturnNewBookView() throws Exception {
        mockMvc.perform(get("/showNewBookForm").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("new_book"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    @WithMockUser
    void showFormForUpdate_shouldReturnUpdateView_WhenIdExists() throws Exception {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act & Assert
        mockMvc.perform(get("/showFormForUpdate/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("update_book"))
                .andExpect(model().attribute("book", book));
    }

    // --- BRANCHES FOR deleteBook() ---

    @Test
    @WithMockUser
    void deleteBook_shouldRedirectToAdminBooks() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/deleteBook/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/books"));

        // Verify repository was called
        verify(bookRepository, times(1)).deleteById(1L);
    }
}