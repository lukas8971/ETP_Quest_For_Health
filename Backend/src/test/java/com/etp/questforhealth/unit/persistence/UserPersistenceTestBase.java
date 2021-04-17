package com.etp.questforhealth.unit.persistence;

import com.etp.questforhealth.persistence.UserDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public abstract class UserPersistenceTestBase {

    @Autowired
    UserDao userDao;

    @Test
    @DisplayName("Requesting an empty user list should return null")
    public void requestingEmptyUserList_shouldReturnNull() {
        assertEquals(new ArrayList<>(), userDao.getAll());
    }
}
