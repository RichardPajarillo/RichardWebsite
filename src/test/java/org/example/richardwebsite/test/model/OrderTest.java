package org.example.richardwebsite.test.model;

import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.OrderItem;
import org.example.richardwebsite.model.OrderStatus;
import org.example.richardwebsite.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

        order.setTotal(150.0);
        order.setStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setUserOrderNumber(101);

        assertEquals(150.0, order.getTotal());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(user, order.getUser());
        assertEquals(101, order.getUserOrderNumber());
    }
}