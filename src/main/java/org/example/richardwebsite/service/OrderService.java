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
        // Branches 1 & 2: max is null vs max has value
        int nextNumber = (max == null) ? 1 : max + 1;

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(user.getUsername());
        order.setUserOrderNumber(nextNumber);
        order.setStatus(OrderStatus.PENDING);

        // calculateTotal handles null/empty carts safely
        BigDecimal total = calculateTotal(cart);
        order.setTotal(total);

        // FIX: Added null check to prevent NPE (Branches 5 & 6)
        if (cart != null && cart.getItems() != null) {
            // Branches 7 & 8: Loop logic (has items vs no items)
            for (CartItem cartItem : cart.getItems()) {
                OrderItem item = new OrderItem(
                        cartItem.getBook().getTitle(),
                        cartItem.getBook().getPrice(),
                        cartItem.getQuantity()
                );
                order.addItem(item);
            }
        }

        return orderRepository.save(order);
    }

    private BigDecimal calculateTotal(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        // Branches 3 & 4: cart is null or items list is null
        if (cart == null || cart.getItems() == null) {
            return total;
        }

        // Branches 9 & 10: Loop logic (has items vs no items)
        for (CartItem cartItem : cart.getItems()) {
            BigDecimal price = BigDecimal.valueOf(cartItem.getBook().getPrice());
            BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
            total = total.add(price.multiply(quantity));
        }
        return total;
    }
}