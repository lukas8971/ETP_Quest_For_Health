package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.PersistenceException;

import java.util.List;

public interface UserDao {

    List<User> getAll();

    User createUser(User user) throws PersistenceException;

    void rollbackChanges();
}
