package org.example.richardwebsite.model;

import jakarta.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookTitle;

    private Double price;

    private Integer quantity;

    @ManyToOne
    private Order order;

    public OrderItem() {}

    public OrderItem(String bookTitle, Double price, Integer quantity) {
        this.bookTitle = bookTitle;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() { return id; }

    public String getBookTitle() { return bookTitle; }

    public Double getPrice() { return price; }

    public Integer getQuantity() { return quantity; }

    public void setOrder(Order order) {
        this.order = order;
    }
}