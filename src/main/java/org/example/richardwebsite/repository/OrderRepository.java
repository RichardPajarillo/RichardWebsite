package org.example.richardwebsite.repository;

import org.example.richardwebsite.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser_Id(Long userId);
    int countByUser_Id(Long userId);

    @Query("SELECT MAX(o.userOrderNumber) FROM Order o WHERE o.user.id = :userId")
    Integer findMaxUserOrderNumber(Long userId);
}