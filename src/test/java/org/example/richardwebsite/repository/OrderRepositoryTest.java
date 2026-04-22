package org.example.richardwebsite.repository;

import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUser_Id_shouldReturnOrdersForUser() {
        // Arrange
        User user = new User("username", "password", "USER");
        user = userRepository.save(user);
        Order order1 = new Order();
        order1.setUser(user);
        Order order2 = new Order();
        order2.setUser(user);
        orderRepository.save(order1);
        orderRepository.save(order2);

        // Act
        List<Order> result = orderRepository.findByUser_Id(user.getId());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order2));
    }

    @Test
    void countByUser_Id_shouldReturnCount() {
        // Arrange
        User user = new User("username", "password", "USER");
        user = userRepository.save(user);
        Order order = new Order();
        order.setUser(user);
        orderRepository.save(order);

        // Act
        int result = orderRepository.countByUser_Id(user.getId());

        // Assert
        assertEquals(1, result);
    }

    @Test
    void findMaxUserOrderNumber_shouldReturnMax() {
        // Arrange
        User user = new User("username", "password", "USER");
        user = userRepository.save(user);
        Order order1 = new Order();
        order1.setUser(user);
        order1.setUserOrderNumber(5);
        Order order2 = new Order();
        order2.setUser(user);
        order2.setUserOrderNumber(10);
        orderRepository.save(order1);
        orderRepository.save(order2);

        // Act
        Integer result = orderRepository.findMaxUserOrderNumber(user.getId());

        // Assert
        assertEquals(10, result);
    }

    @Test
    void findMaxUserOrderNumber_shouldReturnNullWhenNoOrders() {
        // Arrange
        User user = new User("username", "password", "USER");
        user = userRepository.save(user);

        // Act
        Integer result = orderRepository.findMaxUserOrderNumber(user.getId());

        // Assert
        assertNull(result);
    }
}