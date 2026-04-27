package org.example.richardwebsite.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    // BUSINESS LOGIC
    public void addItem(Book book, int qty) {

        for (CartItem item : items) {
            if (item.getBook().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }

        CartItem newItem = new CartItem(book, qty, this);
        items.add(newItem);
    }

    public void removeItem(Long bookId) {
        items.removeIf(item ->
                item.getBook().getId().equals(bookId)
        );
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(i -> i.getBook().getPrice() * i.getQuantity())
                .sum();
    }
}