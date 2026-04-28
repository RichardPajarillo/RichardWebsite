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

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return;
        }

        Order order = new Order();
        order.setUser(securityService.getCurrentUser());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cart.getItems()) {

            BigDecimal price = ci.getBook().getPrice();
            BigDecimal quantity = BigDecimal.valueOf(ci.getQuantity());

            OrderItem item = new OrderItem(
                    ci.getBook().getTitle(),
                    price,
                    ci.getQuantity()
            );

            order.addItem(item);

            total = total.add(price.multiply(quantity));
        }

        order.setTotal(total);

        // ✅ THIS WAS MISSING
        orderRepository.save(order);

        // optional but expected by your test
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}