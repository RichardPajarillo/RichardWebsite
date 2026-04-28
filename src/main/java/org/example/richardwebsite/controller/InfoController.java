package org.example.richardwebsite.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/news")
    public String news() {
        return "news";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }


}