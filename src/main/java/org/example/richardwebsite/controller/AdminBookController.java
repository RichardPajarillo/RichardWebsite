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

@Controller
public class AdminBookController { // RENAME THIS to avoid ambiguity

    private final BookRepository bookRepository;

    public AdminBookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // This handles the Admin Table View with Pagination
    @GetMapping("/admin/books")
    public String adminBooks(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        int size = 10; // Show 10 books per page in admin table
        Page<Book> bookPage = bookRepository.findAll(PageRequest.of(page, size));

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("books", bookPage.getContent()); // Matches your th:each="book : ${books}"

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

        if (result.hasErrors()) {
            model.addAttribute("priceError", "Invalid Input!");
            return book.getId() == null ? "new_book" : "update_book";
        }

        // 1. Capture whether this is an update before saving
        Long originalId = book.getId();

        bookRepository.save(book);

        // 2. Set the message dynamically based on whether it was an update
        String successMessage = (originalId == null)
                ? "Book saved successfully!"
                : "Book updated successfully!";

        redirectAttributes.addFlashAttribute("message", successMessage);

        // 3. Handle redirects based on update vs new
        if (originalId != null) {
            return "redirect:/admin/books/update/" + originalId;
        }

        return "redirect:/admin/books/new";
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