package com.gjm.file_cloud.service;

import com.gjm.file_cloud.dao.UserDao;
import com.gjm.file_cloud.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<User> findAllUsers() {
        return userDao.findAll();
    }
}
