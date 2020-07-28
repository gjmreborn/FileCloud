package com.gjm.file_cloud.helpers;

import com.gjm.file_cloud.dao.RoleDao;
import com.gjm.file_cloud.dao.UserDao;
import com.gjm.file_cloud.entity.Role;
import com.gjm.file_cloud.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
public class AdministratorsManager {
    private UserDao userDao;
    private RoleDao roleDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdministratorsManager(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void manageAdministrators() {
        Role adminRole = roleDao.findByRole("ROLE_ADMIN");
        if(adminRole == null) {
            adminRole = roleDao.save(new Role("ROLE_ADMIN"));
        }

        if(userDao.findByUsername("gjm_admin") == null) {
            User adminUser = new User(
                    "gjm_admin",
                    passwordEncoder.encode("admin"),
                    Collections.singletonList(adminRole),
                    Collections.emptyList()
            );

            userDao.save(adminUser);
        }
    }
}
