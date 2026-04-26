package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.Order;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.CartRepository;
import org.example.richardwebsite.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.example.richardwebsite.repository.OrderRepository;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository; // ADD THIS
    private final PasswordEncoder passwordEncoder;

    // UPDATE the constructor to include orderRepository
    public AdminUserController(UserRepository userRepository,
                               CartRepository cartRepository,
                               OrderRepository orderRepository, // ADD THIS
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository; // ADD THIS
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String manageUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        int size = 10;
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));

        model.addAttribute("userPage", userPage);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("baseUrl", "/admin/users");

        return "admin-users";
    }

    // CREATE: Show Form
    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin-add-user";
    }

    // CREATE: Process Form
    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute User user, BindingResult result) { // 2. Add @Valid and BindingResult

        // 3. Check for validation errors (like @NotBlank failing due to spaces)
        if (result.hasErrors()) {
            return "admin-add-user"; // 4. Return to the form instead of crashing
        }

        // 2. CHECK FOR DUPLICATE HERE
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            // This adds the error to the 'username' field specifically
            result.rejectValue("username", "error.user", "This username is already taken.");

            // Return the view name directly (Status 200) so the test passes
            return "admin-add-user";
        }

        userRepository.save(user);
        return "redirect:/admin/users";
    }

    // UPDATE: Change Role
    @PostMapping("/updateRole")
    public String updateRole(@RequestParam Long userId, @RequestParam String newRole, Principal principal) {
        // Get the user intended for the update
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the current admin is trying to change their own role
        if (userToUpdate.getUsername().equals(principal.getName())) {
            return "redirect:/admin/users?error=selfRoleChange";
        }

        userToUpdate.setRole(newRole);
        userRepository.save(userToUpdate);
        return "redirect:/admin/users";
    }

    // DELETE: Remove User
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, Principal principal) {
        String currentUsername = principal.getName();
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userToDelete.getUsername().equals(currentUsername)) {
            return "redirect:/admin/users?error=selfDelete";
        }

        // 1. Disconnect all orders from this user
        // This keeps the orders in the database but sets their user_id to NULL
        for (Order order : userToDelete.getOrders()) {
            order.setUser(null);
            orderRepository.save(order);
        }

        // 2. The Cart should still be deleted (users usually don't need orphan carts)
        if (userToDelete.getCart() != null) {
            cartRepository.delete(userToDelete.getCart());
        }

        // 3. Now it's safe to delete the user
        userRepository.delete(userToDelete);

        return "redirect:/admin/users";
    }
}