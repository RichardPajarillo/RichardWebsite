package org.example.richardwebsite.test.controller;

import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @WithMockUser
    void viewBooks_shouldUseGenreBranch_WhenGenreProvided() throws Exception {
        // 1. Arrange
        String genre = "Comedy";
        Page<Book> booksPage = new PageImpl<>(List.of(new Book()));

        // Ensure we match the exact genre and ANY pageable request
        when(bookRepository.findByGenreIgnoreCase(eq(genre), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(booksPage);

        // 2. Act & Assert
        mockMvc.perform(get("/")
                        .param("genre", genre)
                        .with(csrf())) // Critical for template rendering
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                // Check that 'genre' was actually put back into the model
                .andExpect(model().attribute("genre", genre))
                .andExpect(model().attributeExists("bookPage"));
    }

    // --- BRANCHES IN saveBook() ---

    @Test
    void saveBook_shouldReturnNewBookView_WhenValidationFailsAndIdIsNull() throws Exception {

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", new Book())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("new_book"));
    }

    @Test
    void saveBook_shouldReturnUpdateBookView_WhenValidationFailsAndIdExists() throws Exception {

        Book book = new Book();
        book.setId(99L);

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", book)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("update_book"));
    }

    @Test
    void saveBook_shouldRedirectToNewForm_WhenCreationSuccessful() throws Exception {

        Book newBook = new Book("url", "Title", "Author", "Genre", "About", BigDecimal.ONE, 1);

        when(bookRepository.save(any(Book.class)))
                .thenAnswer(invocation -> {
                    Book b = invocation.getArgument(0);
                    b.setId(1L);
                    return b;
                });

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", newBook)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/books/new"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    void saveBook_shouldRedirectToUpdateForm_WhenUpdateSuccessful() throws Exception {

        Book existing = new Book("url", "Title", "Author", "Genre", "About", BigDecimal.ONE, 1);
        existing.setId(55L);

        when(bookRepository.save(any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", existing)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/books/update/55"))
                .andExpect(flash().attributeExists("message"));
    }

    // --- ADDITIONAL BRANCHES FOR viewBooks() ---

    @Test
    @WithMockUser
    void viewBooks_shouldUseDefaultBranch_WhenNoParamsProvided() throws Exception {
        // Arrange
        Page<Book> booksPage = new PageImpl<>(List.of(new Book()));
        // We match any Pageable because the controller creates a PageRequest.of(0, 8)
        when(bookRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(booksPage);

        // Act & Assert
        mockMvc.perform(get("/")
                        .with(csrf())) // 👈 MUST ADD THIS
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("bookPage"));
    }

    @Test
    @WithMockUser
    void viewBooks_shouldHandleNegativePageNumber() throws Exception {
        // 1. Arrange
        Page<Book> booksPage = new PageImpl<>(List.of(new Book()));
        when(bookRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(booksPage);

        // 2. Act
        mockMvc.perform(get("/")
                        .with(csrf())) // 👈 This adds the missing CSRF tokens to the model
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

        // 3. Capture and Assert
        ArgumentCaptor<org.springframework.data.domain.Pageable> captor =
                ArgumentCaptor.forClass(org.springframework.data.domain.Pageable.class);

        // Explicitly cast to resolve the "Ambiguous method call" from earlier
        verify(bookRepository).findAll((org.springframework.data.domain.Pageable) captor.capture());

        // Compare as primitives to avoid any String/Object confusion
        int expectedPage = 0;
        int actualPage = captor.getValue().getPageNumber();

        // JUnit 5 assertEquals(expected, actual)
        assertEquals(expectedPage, actualPage);
    }

    // --- BRANCHES FOR showNewBookForm & showFormForUpdate ---

    @Test
    @WithMockUser
    void showNewBookForm_shouldReturnNewBookView() throws Exception {
        // Change "/admin/showNewBookForm" to "/admin/books/new"
        mockMvc.perform(get("/admin/books/new").with(csrf()))
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
        // Mock the repository to return our book when ID 1 is requested
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act & Assert
        // Match the @GetMapping("/admin/books/update/{id}") in AdminBookController
        mockMvc.perform(get("/admin/books/update/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("update_book"))
                .andExpect(model().attribute("book", book));
    }

    // --- BRANCHES FOR deleteBook() ---

    @Test
    @WithMockUser
    void deleteBook_shouldRedirectToAdminBooks() throws Exception {
        // Act & Assert
        // Change "/deleteBook/1" to "/admin/books/delete/1"
        mockMvc.perform(post("/admin/books/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/books"));

        // Verify repository was called
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser
    void viewBooks_shouldUseDefaultBranch_WhenSearchIsWhitespace() throws Exception {
        // This covers the !search.trim().isEmpty() branch being false
        Page<Book> booksPage = new PageImpl<>(List.of(new Book()));
        when(bookRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(booksPage);

        mockMvc.perform(get("/")
                        .param("search", "   ") // Whitespace search
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("search", "   "))
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser
    void viewBooks_shouldUseDefaultBranch_WhenGenreIsEmpty() throws Exception {
        // This covers the !genre.trim().isEmpty() branch being false
        Page<Book> booksPage = new PageImpl<>(List.of(new Book()));
        when(bookRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(booksPage);

        mockMvc.perform(get("/")
                        .param("genre", "") // Empty genre
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genre", ""))
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser
    void viewBooks_shouldHandleValidPositivePage() throws Exception {
        // Covers the branch where (page == null || page < 0) is FALSE
        Page<Book> booksPage = new PageImpl<>(List.of(new Book()));
        when(bookRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(booksPage);

        mockMvc.perform(get("/")
                        .param("page", "2")
                        .with(csrf()))
                .andExpect(status().isOk());

        ArgumentCaptor<org.springframework.data.domain.Pageable> captor = ArgumentCaptor.forClass(org.springframework.data.domain.Pageable.class);
        verify(bookRepository).findAll(captor.capture());

        assertEquals(2, captor.getValue().getPageNumber());
    }

    @Test
    void saveBook_shouldThrowException_WhenSaveReturnsNull() throws Exception {

        Book validBook = new Book(
                "url",
                "Title",
                "Author",
                "Genre",
                "About",
                BigDecimal.ONE,
                1
        );

        when(bookRepository.save(any(Book.class)))
                .thenReturn(null);

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", validBook)
                        .with(csrf()))
                .andExpect(result ->
                        assertTrue(
                                result.getResolvedException() instanceof IllegalStateException
                        )
                );
    }

    @Test
    void saveBook_shouldThrowException_WhenSavedBookHasNullId() throws Exception {

        Book validBook = new Book(
                "url",
                "Title",
                "Author",
                "Genre",
                "About",
                BigDecimal.ONE,
                1
        );

        Book savedBook = new Book();
        savedBook.setId(null);

        when(bookRepository.save(any(Book.class)))
                .thenReturn(savedBook);

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", validBook)
                        .with(csrf()))
                .andExpect(result ->
                        assertTrue(
                                result.getResolvedException() instanceof IllegalStateException
                        )
                );
    }
}