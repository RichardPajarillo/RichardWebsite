package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.OrderStatus;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.OrderRepository;
import org.example.richardwebsite.service.SecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {

    private final OrderRepository orderRepository;
    private final SecurityService securityService;

    public OrderController(OrderRepository orderRepository,
                           SecurityService securityService) {
        this.orderRepository = orderRepository;
        this.securityService = securityService;
    }

    // --- USER VIEW: My Orders ---
    @GetMapping("/orders")
    public String orders(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        User user = securityService.getCurrentUser();

        int size = 6; // Fewer items per page since cards are large
        // Sort by ID descending so newest orders appear first
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Order> orderPage = orderRepository.findByUser_Id(user.getId(), pageable);

        model.addAttribute("orderPage", orderPage);
        model.addAttribute("orders", orderPage.getContent());

        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        Order order = orderRepository.findById(id).orElseThrow();
        User currentUser = securityService.getCurrentUser();

        // 🔒 SECURITY: Prevent viewing other users' orders
        if (!order.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "order-details";
    }

    // --- ADMIN VIEW: Manage All Orders ---
    @GetMapping("/admin/orders")
    public String adminOrders(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model) {

        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Order> orderPage;

        if (search != null && !search.trim().isEmpty()) {

            orderPage = orderRepository
                    .findByCustomerNameContainingIgnoreCaseOrUserOrderNumberContainingIgnoreCase(
                            search, search, pageable);

        } else {

            orderPage = orderRepository.findAll(pageable);
        }

        model.addAttribute("orderPage", orderPage);
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("search", search); // 🔥 important for input persistence

        return "admin-orders";
    }

    // Handle status updates from the admin table (matching your admin-orders.html fetch script)
    @PostMapping("/admin/orders/update-status")
    @ResponseBody
    public void updateStatus(@RequestParam Long orderId, @RequestParam String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Convert the String "PENDING", "SHIPPED", etc., into the Enum type
        try {
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
            orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            // Handle the case where the string doesn't match any Enum value
            throw new RuntimeException("Invalid order status: " + status);
        }
    }

    // Handle order deletion from the admin table
    @PostMapping("/admin/orders/delete/{id}")
    @ResponseBody // This is required for the JavaScript fetch() to work without a page reload
    public void deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
    }


}