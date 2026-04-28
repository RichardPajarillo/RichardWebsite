package org.example.richardwebsite.test.controller;

import org.example.richardwebsite.model.Book;
import org.junit.jupiter.api.Test;
import org.example.richardwebsite.repository.BookRepository;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;


import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AdminBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRepository bookRepository;

    // =====================================================
    // 🔥 manageBooks() — ALL 4 BRANCHES
    // =====================================================

    @Test
    void manageBooks_shouldUseAllFilters() throws Exception {

        when(bookRepository.findByTitleContainingIgnoreCaseAndGenreIgnoreCase(any(), any(), any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/admin/books")
                        .param("search", "a")
                        .param("genre", "b"))
                .andExpect(status().isOk());
    }

    @Test
    void manageBooks_shouldUseSearchOnly() throws Exception {

        when(bookRepository.findByTitleContainingIgnoreCase(any(), any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/admin/books")
                        .param("search", "a"))
                .andExpect(status().isOk());
    }

    @Test
    void manageBooks_shouldUseGenreOnly() throws Exception {

        when(bookRepository.findByGenreIgnoreCase(any(), any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/admin/books")
                        .param("genre", "a"))
                .andExpect(status().isOk());
    }



    // =====================================================
    // 🔥 showNewBookForm
    // =====================================================

    @Test
    void showNewBookForm_shouldReturnView() throws Exception {

        mockMvc.perform(get("/admin/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("new_book"));
    }

    // =====================================================
    // 🔥 showFormForUpdate — success + failure
    // =====================================================

    @Test
    void showFormForUpdate_shouldReturnBook() throws Exception {

        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        mockMvc.perform(get("/admin/books/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update_book"));
    }

    @Test
    void showFormForUpdate_shouldThrowException() throws Exception {

        when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/books/update/99"))
                .andExpect(status().is5xxServerError());
    }

    // =====================================================
    // 🔥 deleteBook
    // =====================================================

    @Test
    void deleteBook_shouldRedirect() throws Exception {

        mockMvc.perform(post("/admin/books/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    // =====================================================
    // 🔥 saveBook — ALL BRANCHES
    // =====================================================

    // 1. validation fail → new
    @Test
    void saveBook_validationFail_new() throws Exception {

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", new Book())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("new_book"));
    }

    // 2. validation fail → update
    @Test
    void saveBook_validationFail_update() throws Exception {

        Book b = new Book();
        b.setId(5L);

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", b)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("update_book"));
    }

    // 3. create success
    @Test
    void saveBook_create_success() throws Exception {

        Book b = new Book("u","t","a","g","a",BigDecimal.ONE,1);

        when(bookRepository.save(any()))
                .thenAnswer(inv -> {
                    Book x = inv.getArgument(0);
                    x.setId(1L);
                    return x;
                });

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", b)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    // 4. update success
    @Test
    void saveBook_update_success() throws Exception {

        Book b = new Book("u","t","a","g","a",BigDecimal.ONE,1);
        b.setId(10L);

        when(bookRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", b)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    // 5. save returns null (🔥 exception branch)
    @Test
    void saveBook_saveReturnsNull_shouldThrow() throws Exception {

        Book valid = new Book("u","t","a","g","a",BigDecimal.ONE,1);

        when(bookRepository.save(any()))
                .thenReturn(null);

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", valid)
                        .with(csrf()))
                .andExpect(status().is5xxServerError());
    }

    // 6. save returns null id (🔥 exception branch)
    @Test
    void saveBook_saveReturnsNullId_shouldThrow() throws Exception {

        Book valid = new Book("u","t","a","g","a",BigDecimal.ONE,1);

        when(bookRepository.save(any()))
                .thenReturn(new Book());

        mockMvc.perform(post("/saveBook")
                        .flashAttr("book", valid)
                        .with(csrf()))
                .andExpect(status().is5xxServerError());
    }
}
