package org.example.richardwebsite.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    private Double total;

    public Order() {}

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public Long getId() { return id; }

    public List<OrderItem> getItems() { return items; }

    public Double getTotal() { return total; }

    public void setTotal(Double total) { this.total = total; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}