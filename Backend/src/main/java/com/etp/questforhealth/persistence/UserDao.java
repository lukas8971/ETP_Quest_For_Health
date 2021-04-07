package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.User;

import java.util.List;

public interface UserDao {

    List<User> getAll();
}
