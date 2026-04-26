package org.example.richardwebsite.service;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(User user, Cart cart) {
        Integer max = orderRepository.findMaxUserOrderNumber(user.getId());
        int nextNumber = (max == null) ? 1 : max + 1;

        Order order = new Order();
        order.setUser(user);

        // 1. ADD THE SNAPSHOT: This saves the username forever
        order.setCustomerName(user.getUsername());

        order.setUserOrderNumber(nextNumber);
        order.setStatus(OrderStatus.PENDING);

        // 2. USE THE TOTAL CALCULATION:
        // Since you already have a loop, we calculate it right here
        BigDecimal total = calculateTotal(cart);
        order.setTotal(total);

        // Add items to the order
        for (CartItem cartItem : cart.getItems()) {
            OrderItem item = new OrderItem(
                    cartItem.getBook().getTitle(),
                    cartItem.getBook().getPrice(),
                    cartItem.getQuantity()
            );
            order.addItem(item);
        }

        return orderRepository.save(order);
    }

    // 3. ADD THIS METHOD: The BigDecimal version of calculateTotal
    private BigDecimal calculateTotal(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        if (cart == null || cart.getItems() == null) {
            return total;
        }

        for (CartItem cartItem : cart.getItems()) {
            BigDecimal price = BigDecimal.valueOf(cartItem.getBook().getPrice());
            BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
            total = total.add(price.multiply(quantity));
        }
        return total;
    }
}