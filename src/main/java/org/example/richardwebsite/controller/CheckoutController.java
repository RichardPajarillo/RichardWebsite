package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.*;
import org.example.richardwebsite.service.OrderService;
import org.example.richardwebsite.service.SecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final SecurityService securityService;
    private final OrderService orderService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CheckoutController(SecurityService securityService,
                              OrderService orderService,
                              CartRepository cartRepository, CartItemRepository cartItemRepository) {

        this.securityService = securityService;
        this.orderService = orderService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @PostMapping
    public String checkout() {

        User user = securityService.getCurrentUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return "redirect:/cart?error=empty";
        }

        orderService.createOrder(user, cart);

        cart.getItems().clear();
        cartRepository.save(cart);

        return "redirect:/orders";
    }

    @GetMapping
    public String showCheckout(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        User user = securityService.getCurrentUser();
        Cart cart = cartRepository.findByUser_Id(user.getId()).orElseThrow();

        int size = 10;
        Page<CartItem> itemPage = cartItemRepository.findByCart_Id(cart.getId(), PageRequest.of(page, size));

        model.addAttribute("cart", cart);
        model.addAttribute("itemPage", itemPage);
        model.addAttribute("baseUrl", "/checkout");

        return "checkout";
    }

}