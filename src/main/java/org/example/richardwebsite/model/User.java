package org.example.richardwebsite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3-50 characters long")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 50, message = "Password must be at least 3-50 characters long")
    @Column(nullable = false)
    private String password;


    //orphanator
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    // Change this line to REMOVE CascadeType.ALL and orphanRemoval
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    // USER or ADMIN
    private String role = "USER"; // default safety

    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    // 3. Add Getter and Setter for Orders
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null) {
            this.username = username.trim(); // Removes leading and trailing spaces
        } else {
            this.username = null;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        if (password != null) {
            this.password = password.trim(); // Removes spaces before saving/validating
        } else {
            this.password = null;
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Add this setter (useful for creating carts later)
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }
}