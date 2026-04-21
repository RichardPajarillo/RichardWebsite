package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.OrderStatus;
import org.example.richardwebsite.repository.OrderRepository;
import org.example.richardwebsite.service.SecurityService;
import org.example.richardwebsite.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderRepository orderRepository;
    private final SecurityService securityService;
    private final BookRepository bookRepository;

    public AdminController(OrderRepository orderRepository,
                           SecurityService securityService,
                           BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.securityService = securityService;
        this.bookRepository = bookRepository;
    }



    // DASHBOARD PAGE
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin-dashboard";
    }


    // ALL ORDERS
    @GetMapping("/orders")
    public String allOrders(Model model) {
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        // This allows the <select> to see all possible status options
        model.addAttribute("statusValues", OrderStatus.values());
        return "admin-orders";
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

    // delete orders

    @PostMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return "redirect:/admin/orders";
    }


}