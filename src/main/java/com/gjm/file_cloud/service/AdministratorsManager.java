package com.gjm.file_cloud.service;

import com.gjm.file_cloud.dao.RoleDao;
import com.gjm.file_cloud.dao.UserDao;
import com.gjm.file_cloud.entity.Role;
import com.gjm.file_cloud.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AdministratorsManager {
    @Value("${file_cloud.admin_name}")
    private String adminName;

    @Value("${file_cloud.admin_password}")
    private String adminPassword;

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void manageAdministrators() {
        Role adminRole = checkAdminRole();
        checkAdminExistence(adminRole);
    }

    private Role checkAdminRole() {
        return roleDao.findByRole("ROLE_ADMIN")
                .orElseGet(() -> roleDao.save(new Role("ROLE_ADMIN")));
    }

    private void checkAdminExistence(Role adminRole) {
        if(userDao.findByUsername(adminName).isEmpty()) {
            User adminUser = new User(
                    adminName,
                    passwordEncoder.encode(adminPassword),
                    Collections.singletonList(adminRole),
                    Collections.emptyList()
            );

            userDao.save(adminUser);
        }
    }
}
