package com.etp.questforhealth.unit.service;

import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.service.QuestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public abstract class QuestServiceTestBase {

    @Autowired
    QuestService questService;

    @Test
    @DisplayName("Requesting a not existing quest should throw NotFoundException")
    public void requestingNotExistingQuest_shouldThrowNotFoundException(){
        assertThrows(NotFoundException.class, () -> {
            questService.getOneById(32);
        });
    }
}
