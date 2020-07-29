package com.gjm.file_cloud.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    public String getUsernameOfLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public boolean isUserLoggedIn() {
        return !(SecurityContextHolder.getContext()
                .getAuthentication() instanceof AnonymousAuthenticationToken);
    }
}
