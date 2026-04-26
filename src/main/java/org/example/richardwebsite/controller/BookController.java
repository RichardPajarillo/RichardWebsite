package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Keep ONLY the main store view here
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
            booksPage = bookRepository.findByTitleContainingIgnoreCase(search, PageRequest.of(pageNumber, size));
        } else if (genre != null && !genre.trim().isEmpty()) {
            booksPage = bookRepository.findByGenreIgnoreCase(genre, PageRequest.of(pageNumber, size));
        } else {
            booksPage = bookRepository.findAll(PageRequest.of(pageNumber, size));
        }

        model.addAttribute("bookPage", booksPage);
        model.addAttribute("genre", genre);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", pageNumber);

        return "index";
    }
}