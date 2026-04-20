package org.example.richardwebsite.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(Book book, int qty) {

        for (CartItem item : items) {
            if (item.getBook().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }

        CartItem newItem = new CartItem();
        newItem.setBook(book);
        newItem.setQuantity(qty);
        newItem.setCart(this);

        items.add(newItem);

        // maybe this is the issue?
        newItem.getCart().getItems().add(newItem);
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

    public void clear() {
        items.clear();
    }
}