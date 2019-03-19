package com.gjm.file_cloud.helpers;

import com.gjm.file_cloud.dao.FileDao;
import com.gjm.file_cloud.dao.RoleDao;
import com.gjm.file_cloud.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseCleaner {
    private FileDao fileDao;
    private UserDao userDao;
    private RoleDao roleDao;

    @Autowired
    public DatabaseCleaner(FileDao fileDao, UserDao userDao, RoleDao roleDao) {
        this.fileDao = fileDao;
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @PostConstruct
    public void databaseCleaner() {
//        roleDao.deleteAll();
//        fileDao.deleteAll();
//        userDao.deleteAll();
    }
}
