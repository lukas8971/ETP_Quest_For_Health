package com.etp.questforhealth.unit.endpoint;

import com.etp.questforhealth.endpoint.QuestEndpoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class QuestEndpointTest {

    @Autowired
    QuestEndpoint questEndpoint;

    @Test
    @DisplayName("Requesting a not existing quest should throw ResponseStatusException")
    public void requestingNotExistingQuest_shouldResponseStatusException(){
        assertThrows(ResponseStatusException.class, () -> {
            questEndpoint.getOneById(32);
        });
    }
}
