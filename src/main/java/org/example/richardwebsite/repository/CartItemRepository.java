package org.example.richardwebsite.repository;


import org.example.richardwebsite.model.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // This finds items belonging to a specific cart with pagination
    Page<CartItem> findByCart_Id(Long cartId, Pageable pageable);
}