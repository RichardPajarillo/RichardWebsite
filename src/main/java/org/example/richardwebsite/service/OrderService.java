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
        order.setCustomerName(user.getUsername());

        order.setUserOrderNumber(nextNumber); // FIXED (no String)

        order.setStatus(OrderStatus.PENDING);

        BigDecimal total = calculateTotal(cart);
        order.setTotal(total);

        if (cart != null && cart.getItems() != null) {
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
            BigDecimal price = cartItem.getBook().getPrice();
            BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
            total = total.add(price.multiply(quantity));
        }
        return total;
    }
}