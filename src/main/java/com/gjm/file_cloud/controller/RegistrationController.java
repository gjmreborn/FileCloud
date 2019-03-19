package com.gjm.file_cloud.controller;

import com.gjm.file_cloud.entity.User;
import com.gjm.file_cloud.exceptions.RegistrationException;
import com.gjm.file_cloud.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        try {
            registrationService.registerUser(username, password);
            model.addAttribute("registrationResponseMessage", "Successfully registered user " + username);
        } catch(RegistrationException exc) {
            model.addAttribute("registrationResponseMessage", exc.toString());
        }

        return "index";
    }
}
