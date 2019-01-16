package com.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("errorMessage", " ");
        return "security/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("errorMessage", "Invalid logging data");
        return "security/login";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(Model model) {
        model.addAttribute("errorMessage", "Access denied");
        return "security/accessDenied";
    }
}

