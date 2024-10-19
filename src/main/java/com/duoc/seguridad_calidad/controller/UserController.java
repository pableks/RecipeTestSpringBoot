package com.duoc.seguridad_calidad.controller;

import com.duoc.seguridad_calidad.model.User;
import com.duoc.seguridad_calidad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registrationForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/dashboard";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
    try {
        System.out.println("Registering user: " + user.getUsername()); // Debug log
        userService.registerUser(user);
        redirectAttributes.addFlashAttribute("message", "Registration successful. Please log in.");
        return "redirect:/login";
    } catch (Exception e) {
        System.out.println("Registration failed: " + e.getMessage()); // Debug log
        redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
        return "redirect:/users/register";
    }
}

    @GetMapping("/profile")
    public String userProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            user.setUsername(username); // Ensure the username isn't changed
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("message", "Profile updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Profile update failed: " + e.getMessage());
        }
        return "redirect:/users/profile";
    }
}