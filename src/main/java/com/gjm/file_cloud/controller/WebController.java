package com.gjm.file_cloud.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
    @GetMapping({"/", "/home", "/index", "/login"})
    public String mainLogin(@RequestParam(value = "error", required = false, defaultValue = "false") String error, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken)) {
            // User is already logged in
            return "redirect:/file_cloud";
        }

        if(error.equals("true")) {
            model.addAttribute("errorMessage", "Bad login credentials!");
        }

        return "index";
    }

    @GetMapping("/file_cloud")
    public String fileCloud(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("loggedUsername", auth.getName());

        return "file_cloud";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access_denied";
    }
}
