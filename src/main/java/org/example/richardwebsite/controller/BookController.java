package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Controller
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Display list of books with a message
    @GetMapping("/")
    public String viewBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            Model model) {

        int size = 8;

        int pageNumber = (page == null || page < 0) ? 0 : page;

        Page<Book> booksPage;

        if (search != null && !search.trim().isEmpty()) {

            booksPage = bookRepository.findByTitleContainingIgnoreCase(
                    search,
                    PageRequest.of(pageNumber, size)
            );

        } else if (genre != null && !genre.trim().isEmpty()) {

            booksPage = bookRepository.findByGenreIgnoreCase(
                    genre,
                    PageRequest.of(pageNumber, size)
            );

        } else {

            booksPage = bookRepository.findAll(PageRequest.of(pageNumber, size));
        }

        model.addAttribute("listBooks", booksPage.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", booksPage.getTotalPages());

        // IMPORTANT: keep values for pagination links
        model.addAttribute("search", search);
        model.addAttribute("genre", genre);

        return "index";
    }

    // Show new book form
    @GetMapping("/showNewBookForm")
    public String showNewBookForm(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "new_book";
    }

    // Save Book but it also has its own update logic
    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute("book") Book book,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("priceError", "Invalid Price: Please enter numbers only.");
            return book.getId() == null ? "new_book" : "update_book";
        }

        //checker for not null
        boolean isUpdate = (book.getId() != null);


        bookRepository.save(book);

        //checks if it has ID for updated, if not then it is a different message
        if (isUpdate) {
            redirectAttributes.addFlashAttribute("message", "Book updated successfully!");
            return "redirect:/showFormForUpdate/" + book.getId();
        } else {
            redirectAttributes.addFlashAttribute("message", "Book saved successfully!");
            return "redirect:/showNewBookForm";
        }
    }

    // Show update form
    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

        // We put the existing book into the model
        model.addAttribute("book", book);
        return "update_book";
    }

    // Delete book
    @PostMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/";
    }


}