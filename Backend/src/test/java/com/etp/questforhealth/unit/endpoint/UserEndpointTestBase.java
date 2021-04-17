package com.etp.questforhealth.unit.endpoint;

import com.etp.questforhealth.endpoint.UserEndpoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public abstract class UserEndpointTestBase {

    @Autowired
    UserEndpoint userEndpoint;

    @Test
    @DisplayName("Requesting an empty user list should return null")
    public void requestingEmptyUserList_shouldReturnNull() {
        assertNull(userEndpoint.getAll());
    }
}
