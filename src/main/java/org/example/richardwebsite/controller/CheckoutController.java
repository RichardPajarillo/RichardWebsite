package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.*;
import org.example.richardwebsite.service.OrderService;
import org.example.richardwebsite.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final SecurityService securityService;
    private final OrderService orderService;
    private final CartRepository cartRepository;

    public CheckoutController(SecurityService securityService,
                              OrderService orderService,
                              CartRepository cartRepository) {

        this.securityService = securityService;
        this.orderService = orderService;
        this.cartRepository = cartRepository;
    }

    @PostMapping
    public String checkout() {

        User user = securityService.getCurrentUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow();

        orderService.createOrder(user, cart);

        cart.getItems().clear();
        cartRepository.save(cart);

        return "redirect:/orders";
    }
}