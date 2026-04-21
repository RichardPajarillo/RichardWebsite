package org.example.richardwebsite.service;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.*;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final SecurityService securityService;

    public CheckoutService(OrderRepository orderRepository,
                           CartRepository cartRepository,
                           SecurityService securityService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.securityService = securityService;
    }

    public void checkout() {

        Cart cart = cartRepository.findByUser_Id(
                securityService.getCurrentUser().getId()
        ).orElseThrow();

        if (cart.getItems().isEmpty()) return;

        Order order = new Order();
        order.setUser(securityService.getCurrentUser());

        double total = 0;

        for (CartItem ci : cart.getItems()) {

            OrderItem item = new OrderItem(
                    ci.getBook().getTitle(),
                    ci.getBook().getPrice(),
                    ci.getQuantity()
            );

            order.addItem(item);

            total += ci.getBook().getPrice() * ci.getQuantity();
        }

        order.setTotal(total);

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);
    }
}