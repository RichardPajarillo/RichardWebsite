package org.example.richardwebsite.controller;

import org.example.richardwebsite.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.example.richardwebsite.service.SecurityService;
import org.example.richardwebsite.model.User;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final SecurityService securityService;

    public AdminController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {

        if (!securityService.isAdmin()) {
            return "redirect:/access-denied";
        }

        return "admin-dashboard";
    }
}