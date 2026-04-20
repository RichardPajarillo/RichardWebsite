package org.example.richardwebsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller

public class ViewController {

    @GetMapping("/directory")
    public String showDirectory(Model model) {
        model.addAttribute("message", "Welcome to the Directory!");
        return "directory";
    }
}


