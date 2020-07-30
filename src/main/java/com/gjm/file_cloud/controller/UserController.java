package com.gjm.file_cloud.controller;

import com.gjm.file_cloud.entity.User;
import com.gjm.file_cloud.exceptions.RegistrationException;
import com.gjm.file_cloud.service.AuthenticationService;
import com.gjm.file_cloud.service.RegistrationService;
import com.gjm.file_cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public String register(@Valid User user, Model model) {
        try {
            registrationService.registerUser(user);
            model.addAttribute("registrationResponseMessage", "Successfully registered user " + user.getUsername());
        } catch(RegistrationException exc) {
            model.addAttribute("registrationResponseMessage", exc.toString());
        }

        return "index";
    }

    @GetMapping({"/", "/home", "/index", "/login"})
    public String login(@RequestParam(value = "error", required = false, defaultValue = "false") String error, Model model) {
        if(authenticationService.isUserLoggedIn()) {
            return "redirect:/file_cloud";
        }

        if(error.equals("true")) {
            model.addAttribute("errorMessage", "Bad login credentials!");
        }

        return "index";
    }

    @GetMapping("/admin/mainPanel")
    @Secured({"ROLE_ADMIN"})
    public String adminDashboard(Model model) {
        model.addAttribute("loggedAdminUsername", authenticationService.getUsernameOfLoggedInUser());
        model.addAttribute("users", userService.findAllUsers());

        return "admin_panel";
    }

    @GetMapping("/file_cloud")
    public String userDashboard(Model model) {
        model.addAttribute("loggedUsername", authenticationService.getUsernameOfLoggedInUser());

        return "file_cloud";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access_denied";
    }
}
