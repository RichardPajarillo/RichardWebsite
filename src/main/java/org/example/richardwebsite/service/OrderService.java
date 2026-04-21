package org.example.richardwebsite.service;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.OrderRepository;
import org.springframework.stereotype.Service;

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
        order.setUserOrderNumber(nextNumber);

        double total = 0.0;

        // 👉 THIS is your "update service logic"
        for (CartItem cartItem : cart.getItems()) {

            OrderItem item = new OrderItem(
                    cartItem.getBook().getTitle(),
                    cartItem.getBook().getPrice(),
                    cartItem.getQuantity()
            );

            order.addItem(item); // ONLY THIS LINE

            total += cartItem.getBook().getPrice() * cartItem.getQuantity();
        }

        order.setTotal(total);

        return orderRepository.save(order);
    }
}