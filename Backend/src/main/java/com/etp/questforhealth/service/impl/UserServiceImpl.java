package com.etp.questforhealth.service.impl;

import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.persistence.UserDao;
import com.etp.questforhealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public List<User> getAll() {
        return  userDao.getAll();
    }
}
