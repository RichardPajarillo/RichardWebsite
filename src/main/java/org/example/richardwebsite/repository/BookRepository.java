package org.example.richardwebsite.repository;

import org.example.richardwebsite.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    // pagination for all books
    Page<Book> findAll(Pageable pageable);

    // search + pagination
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // genre + pagination
    Page<Book> findByGenreIgnoreCase(String genre, Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCaseAndGenreIgnoreCase(
            String title,
            String genre,
            Pageable pageable
    );

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String author,
            Pageable pageable
    );

}

