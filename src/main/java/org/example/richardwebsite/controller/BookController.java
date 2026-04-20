package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Display list of books with a message
    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("listBooks", bookRepository.findAll());

        // Add this line so your index.html "message" still works!
        model.addAttribute("message", "Hello, Spring Boot with Thymeleaf!");

        return "index";
    }

    // Show new book form
    @GetMapping("/showNewBookForm")
    public String showNewBookForm(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "new_book";
    }

    // Save book
    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute("book") Book book) {
        bookRepository.save(book);
        return "redirect:/";
    }

    // Show update form
    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
        return "update_book";
    }

    // Delete book
    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable(value = "id") Long id) {
        bookRepository.deleteById(id);
        return "redirect:/";
    }


}