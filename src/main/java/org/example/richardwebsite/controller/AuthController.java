package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.User;
import org.example.richardwebsite.service.UserService;
import org.springframework.stereotype.Controller;
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
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // 🔴 REGISTER HANDLER
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String role) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // later BCrypt
        user.setRole(role);

        userService.save(user);

        return "redirect:/login";
    }
}