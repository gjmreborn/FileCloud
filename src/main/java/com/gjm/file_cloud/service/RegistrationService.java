package com.gjm.file_cloud.service;

import com.gjm.file_cloud.dao.RoleDao;
import com.gjm.file_cloud.dao.UserDao;
import com.gjm.file_cloud.entity.Role;
import com.gjm.file_cloud.entity.User;
import com.gjm.file_cloud.exceptions.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RegistrationService {
    private UserDao userDao;
    private RoleDao roleDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String username, String password) {
        if(userDao.findByUsername(username) != null) {
            throw new RegistrationException("User with name " + username + " already exists!");
        }

        Role roleUser = roleDao.findByRole("ROLE_USER");
        if(roleUser == null) {
            roleUser = roleDao.save(new Role("ROLE_USER"));
        }

        User user = new User(
                username,
                passwordEncoder.encode(password),
                true,
                Collections.singletonList(roleUser),
                Collections.emptyList()
        );

        userDao.save(user);
    }
}
