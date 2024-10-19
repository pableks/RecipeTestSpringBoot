package com.duoc.seguridad_calidad.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home(@RequestParam(name="name", required=false, defaultValue="Seguridad y Calidad en el Desarrollo") String name, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // get logged in username

        model.addAttribute("name", name);
        model.addAttribute("username", username);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}