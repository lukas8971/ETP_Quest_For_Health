package com.etp.questforhealth.service.impl;

import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.persistence.UserDao;
import com.etp.questforhealth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public List<User> getAll() {
        LOGGER.trace("getAll()");
        return userDao.getAll();
    }

    @Override
    public List<User> getAllUsersFromDoctor(int doctor){
        LOGGER.trace("getAllUsersFromDoctor({})", doctor);
        return userDao.getAllUsersFromDoctor(doctor);
    }

    @Override
    public User getOneById(int id){
        LOGGER.trace("getOneById({})", id);
        try {
            return userDao.getOneById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
