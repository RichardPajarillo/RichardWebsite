package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.OrderStatus;
import org.example.richardwebsite.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderRepository orderRepository;

    public AdminController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }



    // DASHBOARD PAGE
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin-dashboard";
    }




    // ORDER DETAILS
    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        model.addAttribute("order", order);

        return "admin-order-details";
    }


    //POST mapping to handle status change
    // NEW: Handle the status update form submission
    @PostMapping("/orders/updateStatus")
    public String updateOrderStatus(@RequestParam Long orderId,
                                    @RequestParam OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        orderRepository.save(order);

        return "redirect:/admin/orders";
    }


}