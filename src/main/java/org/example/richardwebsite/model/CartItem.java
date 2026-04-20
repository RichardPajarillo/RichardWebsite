package org.example.richardwebsite.model;

import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Book book;

    private Integer quantity;

    @ManyToOne
    private Cart cart;

    public CartItem() {}

    public CartItem(Book book, Integer quantity, Cart cart) {
        this.book = book;
        this.quantity = quantity;
        this.cart = cart;

        cart.getItems().add(this); // key fix
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}