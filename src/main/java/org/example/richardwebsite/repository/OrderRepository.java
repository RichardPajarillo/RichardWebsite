package org.example.richardwebsite.repository;

import org.example.richardwebsite.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser_Id(Long userId);

    int countByUser_Id(Long userId);

    @Query("SELECT MAX(o.userOrderNumber) FROM Order o WHERE o.user.id = :userId")
    Integer findMaxUserOrderNumber(@Param("userId") Long userId);

    // For User View (Paginated)
    Page<Order> findByUser_Id(Long userId, Pageable pageable);

    // For Admin View (Paginated - uses JpaRepository's findAll(Pageable))
    Page<Order> findAll(Pageable pageable);

    Page<Order> findByCustomerNameContainingIgnoreCaseOrUserOrderNumberContainingIgnoreCase(
            String customerName,
            String orderNumber,
            Pageable pageable
    );
}