package org.example.richardwebsite.controller;

import jakarta.validation.Valid;
import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Pageable;

@Controller
public class AdminBookController { // RENAME THIS to avoid ambiguity

    private final BookRepository bookRepository;

    public AdminBookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // This handles the Admin Table View with Pagination
    @GetMapping("/admin/books")
    public String manageBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<Book> bookPage;

        if (search != null && !search.trim().isEmpty() &&
                genre != null && !genre.trim().isEmpty()) {

            // 🔥 BOTH filters (AND)
            bookPage = bookRepository
                    .findByTitleContainingIgnoreCaseAndGenreIgnoreCase(
                            search, genre, pageable);

        } else if (search != null && !search.trim().isEmpty()) {

            bookPage = bookRepository
                    .findByTitleContainingIgnoreCase(search, pageable);

        } else if (genre != null && !genre.trim().isEmpty()) {

            bookPage = bookRepository
                    .findByGenreIgnoreCase(genre, pageable);

        } else {

            bookPage = bookRepository.findAll(pageable);
        }

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("search", search);
        model.addAttribute("genre", genre);
        model.addAttribute("baseUrl", "/admin/books");

        return "admin-books";
    }

    @GetMapping("/admin/books/new") // Recommended path
    public String showNewBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "new_book"; // This MUST match the HTML file name exactly
    }


    @PostMapping("/saveBook")
    public String saveBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (book.getQuantity() == null) {
            book.setQuantity(0);
        }

        if (result.hasErrors()) {
            System.out.println("VALIDATION ERRORS:");
            result.getAllErrors().forEach(System.out::println);

            model.addAttribute("book", book);
            return "new_book";
        }

        boolean isUpdate = (book.getId() != null);

        bookRepository.save(book);

        redirectAttributes.addFlashAttribute("message",
                isUpdate ? "Book updated!" : "Book saved!");

        return "redirect:/admin/books";
    }



    // 1. Show the Update Form
    @GetMapping("/admin/books/update/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

        model.addAttribute("book", book);
        return "update_book"; // Ensure this file is src/main/resources/templates/update_book.html
    }

    // 2. Handle the Deletion
    @PostMapping("/admin/books/delete/{id}") // or /admin/books/delete/{id}
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/admin/books";
    }
}