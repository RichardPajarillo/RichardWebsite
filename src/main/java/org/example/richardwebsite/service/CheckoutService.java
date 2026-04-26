package org.example.richardwebsite.service;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
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

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cart.getItems()) {
            // Assuming getPrice() returns double or BigDecimal
            BigDecimal price = BigDecimal.valueOf(ci.getBook().getPrice());
            BigDecimal quantity = BigDecimal.valueOf(ci.getQuantity());

            OrderItem item = new OrderItem(
                    ci.getBook().getTitle(),
                    ci.getBook().getPrice(),
                    ci.getQuantity()
            );

            order.addItem(item);

            // Multiply price by quantity and add to total
            total = total.add(price.multiply(quantity));
        }

        order.setTotal(total);

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);
    }
}