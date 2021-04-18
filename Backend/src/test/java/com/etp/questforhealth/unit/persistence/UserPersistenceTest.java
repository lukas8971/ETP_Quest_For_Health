package com.etp.questforhealth.unit.persistence;

import com.etp.questforhealth.persistence.UserDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class UserPersistenceTest {


    @Autowired
    UserDao userDao;

    @Test
    @DisplayName("Requesting an empty user list should return null")
    public void requestingEmptyUserList_shouldReturnNull() {
        assertEquals(new ArrayList<>(), userDao.getAll());
    }
}
