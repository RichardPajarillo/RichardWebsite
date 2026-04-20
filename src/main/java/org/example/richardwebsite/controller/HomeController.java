package org.example.richardwebsite.controller;

import jakarta.servlet.http.HttpSession;
import org.example.richardwebsite.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/Home")
    public String index(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);

        return "index";
    }
}