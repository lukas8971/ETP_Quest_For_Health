package com.etp.questforhealth.unit.service;

import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.service.QuestService;
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
public class QuestServiceTest {

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
