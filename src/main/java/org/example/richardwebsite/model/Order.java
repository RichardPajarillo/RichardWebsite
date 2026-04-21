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

    private Double total;

    private String status;

    // ✅ FIX: user relationship (THIS IS REQUIRED)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_order_number")
    private Integer userOrderNumber;

    // order items
    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this); // CRITICAL
    }

    public Long getId() { return id; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<OrderItem> getItems() { return items; }

    public Integer getUserOrderNumber() {
        return userOrderNumber;
    }

    public void setUserOrderNumber(Integer userOrderNumber) {
        this.userOrderNumber = userOrderNumber;
    }
}