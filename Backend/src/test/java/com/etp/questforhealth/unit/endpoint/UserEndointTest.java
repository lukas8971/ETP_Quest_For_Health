package com.etp.questforhealth.unit.endpoint;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.endpoint.UserEndpoint;
import com.etp.questforhealth.endpoint.dto.UserDto;
import com.etp.questforhealth.entity.User;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class UserEndointTest {


    @Autowired
    UserEndpoint userEndpoint;

    @BeforeAll
    public static void testData(){
        DatabaseTestData.insertTestData();
    }

    @Test
    @DisplayName("Requesting a initial user list should return null")
    public void requestingUserList_shouldReturnNotNull() {
        assertNotNull(userEndpoint.getAll());
    }

    @Test
    @DisplayName("Requesting all users in the database should return a list with 5+6 users")
    public void requestUserList_shouldWork(){
        for (User u: TestData.getNWorkingUsers(5) ){
            userEndpoint.createUser(u);
        }
        List<UserDto> userList = userEndpoint.getAll();
        assertEquals(5+6, userList.size()); // +6 because of initial test data

    }

    @Test
    @DisplayName("Creating a valid user should work")
    public void createUser_shouldWork(){
        UserDto u = userEndpoint.createUser(TestData.getNewWorkingUser());
        assert (u.getId() !=0);
    }

    @Test
    @DisplayName("Creating an invalid user should not work")
    public void createUser_shouldThrowResponseStatusException(){
        assertThrows(ResponseStatusException.class, () -> userEndpoint.createUser(TestData.getNewUser()));
    }
}
