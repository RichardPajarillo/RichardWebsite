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
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem() {}

    public OrderItem(String bookTitle, Double price, Integer quantity) {
        this.bookTitle = bookTitle;
        this.price = price;
        this.quantity = quantity;
    }

    public String getBookTitle() { return bookTitle; }
    public Double getPrice() { return price; }
    public Integer getQuantity() { return quantity; }

    public Order getOrder() {
        return order;
    }

    // ✅ THIS is what you're missing
    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }
}