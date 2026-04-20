package org.example.richardwebsite.service;


import org.example.richardwebsite.model.Book;
import org.example.richardwebsite.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    // Get all Books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get Book by ID
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Save or update Book
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    // Delete Book
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
