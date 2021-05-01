package com.etp.questforhealth.unit.service;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.service.UserService;
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
public class UserServiceTest {

    @BeforeAll
    public static void testData(){
        DatabaseTestData.insertTestData();
    }

    @Autowired
    UserService userService;

    @Test
    @DisplayName("Requesting an empty user list should return null")
    public void requestingEmptyUserList_shouldReturnNull() {
        assertEquals(new ArrayList<>(),userService.getAll());
    }

    @Test
    @DisplayName("Creating a valid user should work")
    public void createUser_shouldWork(){
        User u = userService.createUser(TestData.getNewWorkingUser());
        assert(u.getId()!=0);
    }

    @Test
    @DisplayName("Creating an invalid user should not work")
    public void createUser_shouldThrowValidationException(){
        assertThrows(ValidationException.class, () -> userService.createUser(TestData.getNewUser()));
    }

    @Test
    @DisplayName("Changing the gold amount of a user to still be positive should not throw any exception")
    public void chaningUserGold_shouldNotThrow(){
        assertDoesNotThrow(() -> {
            User u = userService.createUser(TestData.getNewWorkingUserDifferentCharacter());
            assertNotNull(u);
            assertTrue(userService.changeUserGold(u.getId(), 500000));
            assertEquals(userService.getOneById(u.getId()).getCharacterGold(), 500000);
        });
    }
}
