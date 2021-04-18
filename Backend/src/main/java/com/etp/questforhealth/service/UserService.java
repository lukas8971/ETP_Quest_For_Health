package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.ServiceException;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User createUser(User user) throws ServiceException;

    void rollbackChanges();
}
