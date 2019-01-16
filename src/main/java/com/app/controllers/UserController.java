package com.app.controllers;

import com.app.model.Role;
import com.app.model.User;
import com.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUserPost(@ModelAttribute User user, Model model) {
        userService.registerNewUser(user);
        return "redirect:/";
    }
}
