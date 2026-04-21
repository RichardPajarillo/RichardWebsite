package org.example.richardwebsite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cover;
    private String title;
    private String author;
    private String genre;   // ✅ ADDED
    private String about;
    @Positive(message = "Price must be greater than 0")
    private Double price = 0.00;
    private Integer quantity = 0;

    // JPA default constructor
    public Book() {}

    // Full constructor
    public Book(String cover, String title, String author, String genre,
                String about, Double price, Integer quantity) {
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

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}