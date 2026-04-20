package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.OrderRepository;
import org.example.richardwebsite.service.CartService;
import org.example.richardwebsite.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/cart")
public class CheckoutController {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final SecurityService securityService;

    public CheckoutController(CartService cartService,
                              OrderRepository orderRepository,
                              SecurityService securityService) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.securityService = securityService;
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model) {
        model.addAttribute("cart", cartService.getCart());
        return "checkout";
    }

    @PostMapping("/checkout")
    public String confirmCheckout() {

        Cart cart = cartService.getCart();

        if (cart.getItems().isEmpty()) {
            return "redirect:/cart/checkout";
        }

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
        cartService.save(cart);

        return "redirect:/orders";
    }
}