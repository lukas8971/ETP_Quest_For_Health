package com.etp.questforhealth.unit.persistence;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.persistence.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class UserPersistenceTest {

    @Autowired
    UserDao userDao;

    @BeforeAll
    public static void testData(){
        DatabaseTestData.insertTestData();
    }

    @Test
    @DisplayName("Requesting an initial user list should return not null")
    public void requestingEmptyUserList_shouldReturnNull() {
        assertNotNull(userDao.getAll());
    }

    @Test
    @DisplayName("Creating a valid user should work")
    public void createUser_shouldWork(){
        User u = userDao.createUser(TestData.getNewWorkingUser());
        assert(u.getId()!= 0);
    }

    @Test
    @DisplayName("Creating an invalid user should not work")
    public void createUser_shouldThrowPersistenceException(){
        assertThrows(PersistenceException.class, () -> userDao.createUser(TestData.getNewUser()));
    }
}
