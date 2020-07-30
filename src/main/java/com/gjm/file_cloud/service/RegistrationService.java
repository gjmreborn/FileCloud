package com.gjm.file_cloud.service;

import com.gjm.file_cloud.dao.RoleDao;
import com.gjm.file_cloud.dao.UserDao;
import com.gjm.file_cloud.entity.Role;
import com.gjm.file_cloud.entity.User;
import com.gjm.file_cloud.exceptions.RegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        String username = user.getUsername();
        if(userDao.findByUsername(username).isPresent()) {
            throw new RegistrationException("User with name " + username + " already exists!");
        }

        Role userRole = roleDao.findByRole("ROLE_USER")
                .orElseGet(() -> roleDao.save(new Role("ROLE_USER")));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(userRole));
        user.setFiles(Collections.emptyList());

        userDao.save(user);
    }
}
