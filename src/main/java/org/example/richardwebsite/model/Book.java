package org.example.richardwebsite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "URL is required")
    @Size(max = 300, message = "URL too long")
    private String cover;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title too long")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 50, message = "Input too long")
    private String author;

    @NotBlank(message = "Genre is required")
    private String genre;   // ✅ ADDED

    @NotBlank(message = "About is required")
    @Size(max = 500, message = "About section cannot exceed 500 characters")
    private String about;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price = BigDecimal.ZERO;

    @Min(0)
    private Integer quantity = 0;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    // JPA default constructor
    public Book() {}

    // Full constructor
    public Book(String cover, String title, String author, String genre,
                String about, BigDecimal price, Integer quantity) {
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.about = about;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters / Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }   // ✅ ADDED
    public void setGenre(String genre) { this.genre = genre; }

    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}