package org.example.richardwebsite.controller;

import jakarta.validation.Valid;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Add this
import org.springframework.validation.BindingResult; // Add this
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 🔵 LOGIN PAGE (Spring Security handles login POST)
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 🔵 REGISTER PAGE
    // 🔵 Updated: Pass an empty User object to the registration page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // 🔴 REGISTER HANDLER
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model) {

        // 1. If validation fails (like spacebar-only input), return to register.html
        // If password was only spaces, it is now empty and will trigger a validation error
        if (result.hasErrors()) {
            return "register";
        }

        // 2. Check for duplicate username manually
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            // This adds the error to the 'username' field so th:errors can see it
            result.rejectValue("username", "error.user", "Username is already taken.");
            return "register";
        }


        try {
            userService.save(user);
            return "redirect:/login?success";
        } catch (Exception e) {
            model.addAttribute("registrationError", "An unexpected error occurred.");
            return "register";
        }
    }

}