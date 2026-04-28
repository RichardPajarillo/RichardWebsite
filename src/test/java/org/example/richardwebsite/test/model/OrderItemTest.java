package org.example.richardwebsite.test.model;

import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void constructor_shouldStoreSnapshotData() {
        OrderItem item = new OrderItem("Snapshot Title", BigDecimal.ONE, 2);

        assertEquals("Snapshot Title", item.getBookTitle());
        assertEquals(BigDecimal.ONE, item.getPrice());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void setOrder_shouldLinkToOrder() {
        OrderItem item = new OrderItem();
        Order order = new Order();

        item.setOrder(order);

        assertEquals(order, item.getOrder());
    }
}