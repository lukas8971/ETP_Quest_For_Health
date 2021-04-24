package com.etp.questforhealth.unit.endpoint;

import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.endpoint.UserEndpoint;
import com.etp.questforhealth.endpoint.dto.UserDto;
import com.etp.questforhealth.entity.User;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    public void tearDownDBData(){
        userEndpoint.rollbackChanges();
    }

    @Test
    @DisplayName("Requesting an empty user list should return null")
    public void requestingEmptyUserList_shouldReturnNull() {
        assertNull(userEndpoint.getAll());
    }

    @Test
    @DisplayName("Requesting all users in the database should return a list with 5 users")
    public void requestUserList_shouldWork(){
        for (User u: TestData.getNWorkingUsers(5) ){
            userEndpoint.createUser(u);
        }
        List<UserDto> userList = userEndpoint.getAll();
        assertEquals(5, userList.size());

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
