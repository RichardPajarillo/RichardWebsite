package org.example.richardwebsite.View;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller

public class View{
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Hello, Spring Boot with Thymeleaf!");
        return "index";
    }

    @GetMapping("/directory")
    public String showDirectory(Model model) {
        model.addAttribute("message", "Welcome to the Directory!");
        return "directory";
    }
}
