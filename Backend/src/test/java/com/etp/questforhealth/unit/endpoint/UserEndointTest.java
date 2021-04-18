package com.etp.questforhealth.unit.endpoint;

import com.etp.questforhealth.endpoint.UserEndpoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class UserEndointTest {


    @Autowired
    UserEndpoint userEndpoint;

    @Test
    @DisplayName("Requesting an empty user list should return null")
    public void requestingEmptyUserList_shouldReturnNull() {
        assertNull(userEndpoint.getAll());
    }
}
