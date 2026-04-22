package org.example.richardwebsite.test.model;

import org.example.richardwebsite.model.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    void enumValues_shouldBePresent() {
        assertAll(
                () -> assertEquals("PENDING", OrderStatus.PENDING.name()),
                () -> assertEquals("SHIPPED", OrderStatus.SHIPPED.name()),
                () -> assertEquals("DELIVERED", OrderStatus.DELIVERED.name()),
                () -> assertEquals("CANCELLED", OrderStatus.CANCELLED.name())
        );
    }

    @Test
    void valueOf_shouldReturnCorrectConstant() {
        assertEquals(OrderStatus.PENDING, OrderStatus.valueOf("PENDING"));
    }
}