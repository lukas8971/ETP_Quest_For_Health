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

    @Test
    @DisplayName("Updating an existing User should work")
    public void updateUser_shouldWork(){
        User u = userDao.getOneById(1);
        u.setCharacterStrength(5);
        u.setCharacterGold(12);
        u.setCharacterExp(230);
        u.setPassword("Ayyyy");
        u.setFirstname("Hans");
        u.setLastname("Smith");
        u.setCharacterName("Trogdor");
        assertAll(
                () -> assertTrue(userDao.updateUser(u)),
                () -> assertTrue(u.equals(userDao.getOneById(1)))
        );
    }

    @Test
    @DisplayName("Updating a non-existing User should return false")
    public void updateUser_shouldReturnFalse(){
        User u = new User();
        u.setId(-1);
        assertFalse(userDao.updateUser(u));
    }

    @Test
    @DisplayName ("Updating a user with a negative Level should throw a PersistenceException")
    public void updateUser_invalidLevel_shouldReturnPersistenceException(){
        User u = userDao.getOneById(1);
        u.setCharacterLevel(-1);
        assertThrows(PersistenceException.class, () ->userDao.updateUser(u));
    }

    @Test
    @DisplayName ("Updating a user with a negative story-chapter-id should throw a PersistenceException")
    public void updateUser_invalidStoryChapter_shouldReturnPersistenceException(){
        User u = userDao.getOneById(1);
        u.setStoryChapter(-1);
        assertThrows(PersistenceException.class, () ->userDao.updateUser(u));
    }

    @Test
    @DisplayName("Updating gold and exp of an existing user should work")
    public void changeUserGoldAndExp_shouldWork(){
        User u = userDao.getOneById(1);
        int currGold = u.getCharacterGold();
        int currExp = u.getCharacterExp();
        User updatedUser= userDao.changeUserGoldAndExp(u,10,20);
        assertAll(
                () -> assertEquals(currGold +20, updatedUser.getCharacterGold()),
                () -> assertEquals(currExp +10, updatedUser.getCharacterExp())
        );
    }

    @Test
    @DisplayName("Updating gold and exp of a user with an invalid id should throw a PersistenceException")
    public void changeUserGoldAndExp_invalidUser_shouldThrowPersistenceException(){
        User u = new User();
        u.setId(-1);
        assertThrows(PersistenceException.class, ()-> userDao.changeUserGoldAndExp(u, 1,2));
    }


}
