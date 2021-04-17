package com.etp.questforhealth.unit.endpoint;

import com.etp.questforhealth.endpoint.QuestEndpoint;
import com.etp.questforhealth.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

public abstract class QuestEndpointTestBase {

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
