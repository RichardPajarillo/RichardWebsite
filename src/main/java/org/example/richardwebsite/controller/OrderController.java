package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.OrderRepository;
import org.example.richardwebsite.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class OrderController {

    private final OrderRepository orderRepository;
    private final SecurityService securityService;

    public OrderController(OrderRepository orderRepository,
                           SecurityService securityService) {
        this.orderRepository = orderRepository;
        this.securityService = securityService;
    }

    @GetMapping("/orders")
    public String orders(Model model) {

        User user = securityService.getCurrentUser();

        model.addAttribute("orders",
                orderRepository.findByUser_Id(user.getId()));

        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {

        Order order = orderRepository.findById(id)
                .orElseThrow();

        // 🔒 SECURITY (important)
        User currentUser = securityService.getCurrentUser();

        if (!order.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/orders"; // prevent viewing others' orders
        }

        model.addAttribute("order", order);

        return "order-details";
    }


}