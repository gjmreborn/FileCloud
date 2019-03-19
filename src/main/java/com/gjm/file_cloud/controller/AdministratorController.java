package com.gjm.file_cloud.controller;

import com.gjm.file_cloud.dao.UserDao;
import com.gjm.file_cloud.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdministratorController {
    private AuthenticationService authenticationService;
    private UserDao userDao;

    @Autowired
    public AdministratorController(AuthenticationService authenticationService, UserDao userDao) {
        this.authenticationService = authenticationService;
        this.userDao = userDao;
    }

    @GetMapping("/mainPanel")
    @Secured({"ROLE_ADMIN"})
    public String mainPanel(Model model) {
        model.addAttribute("loggedAdminUsername", authenticationService.getUsernameOfLoggedInUser());
        model.addAttribute("users", userDao.findAll());

        return "admin_panel";
    }
}
