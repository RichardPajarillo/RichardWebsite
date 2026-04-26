package org.example.richardwebsite.test.model;

import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.OrderItem;
import org.example.richardwebsite.model.OrderStatus;
import org.example.richardwebsite.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
class OrderTest {

    @Test
    void addItem_shouldEstablishBidirectionalLink() {
        Order order = new Order();
        OrderItem item = new OrderItem("Java Guide", 50.0, 1);

        order.addItem(item);

        assertEquals(1, order.getItems().size());
        assertEquals(order, item.getOrder(), "The item should have a reference back to the order");
    }

    @Test
    void orderProperties_shouldSetAndGet() {
        Order order = new Order();
        User user = new User();

        // 1. Create a BigDecimal instead of a double
        BigDecimal testTotal = new BigDecimal("150.00");

        // 2. Pass the BigDecimal to the setter
        order.setTotal(testTotal);
        order.setStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setUserOrderNumber(101);

        // 3. FIX: Use compareTo to check equality
        // compareTo returns 0 if the values are equal (e.g. 150.0 == 150.00)
        assertNotNull(order.getTotal());
        assertTrue(testTotal.compareTo(order.getTotal()) == 0, "The totals should match numerically");

        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(user, order.getUser());
        assertEquals(101, order.getUserOrderNumber());
    }
}