package com.etp.questforhealth.unit.service;

import com.etp.questforhealth.endpoint.dto.QuestDto;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.QuestDao;
import com.etp.questforhealth.service.QuestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class QuestServiceTest {

    @Autowired
    QuestService questService;
    QuestDao questDao;

    @AfterEach
    public void tearDownDBData(){
        questDao.rollbackChanges();
    }

    @Test
    @DisplayName("Requesting a not existing quest should throw NotFoundException")
    public void requestingNotExistingQuest_shouldThrowNotFoundException(){
        assertThrows(NotFoundException.class, () -> {
            questService.getOneById(99999);
        });
    }


    @Test
    @DisplayName("Inserting a valid quest should return the quest with a positive id.")
    public void insertValidQuest_shouldReturnPositiveId(){
        Quest quest = questService.createQuest(new Quest(0,"UnitQuest","This Quest was created by a Unit Test", 100,10, Duration.parse("PT0S"),0,0,1));
        assertTrue(quest.getId() > 0);
    }

    @Test
    @DisplayName("Inserting invalid characters in the name should return a ValidationException")
    public void insertInvalidName_shouldReturnValidationException(){
        assertThrows(ValidationException.class, () -> {
        questService.createQuest(new Quest(0, "UnitQuest’;", "This Quest was created by a Unit Test", 100, 10, Duration.parse("PT0S"), 0, 0, 1));
        });

    }

    @Test
    @DisplayName("Inserting invalid characters in the description should return a ValidationException")
    public void insertInvalidDescription_shouldReturnValidationException() {
        assertThrows(ValidationException.class, () -> {
            questService.createQuest(new Quest(0, "UnitQuest’;", "This Quest was :created by a Unit Test", 100, 10, Duration.parse("PT0S"), 0, 0, 1));
        });
    }

    @Test
    @DisplayName("Inserting a quest with a negative repetition cycle should return a ValidationException")
    public void insertNegativeDuration_shouldReturnValidationException(){
        assertThrows(ValidationException.class, () -> {
            questService.createQuest(new Quest(0, "UnitQuest", "This Quest was created by a Unit Test", 100, 10, Duration.parse("-PT10M"), 0, 0, 1));
        });
    }

    @Test
    @DisplayName("Inserting a quest with a negative EXP reward should return a ValidationException")
    public void insertNegativeExpReward_shouldReturnValidationException(){
        assertThrows(ValidationException.class, () -> {
            questService.createQuest(new Quest(0, "UnitQuest", "This Quest was created by a Unit Test", -100, 10, Duration.parse("PT0M"), 0, 0, 1));
        });
    }

    @Test
    @DisplayName("Inserting a quest with a negative gold reward should return a ValidationException")
    public void insertNegativeGoldReward_shouldReturnValidationException(){
        assertThrows(ValidationException.class, () -> {
            questService.createQuest(new Quest(0, "UnitQuest", "This Quest was created by a Unit Test", 100, -10, Duration.parse("PT0M"), 0, 0, 1));
        });
    }
    @Test
    @DisplayName("Inserting a quest with a negative EXP penalty should return a ValidationException")
    public void insertNegativeEXPPenalty_shouldReturnValidationException(){
        assertThrows(ValidationException.class, () -> {
            questService.createQuest(new Quest(0, "UnitQuest", "This Quest was created by a Unit Test", 100, 10, Duration.parse("PT0M"), -10, 0, 1));
        });
    }


    @Test
    @DisplayName("Inserting a quest with a negative gold penalty should return a ValidationException")
    public void insertNegativeGoldPenalty_shouldReturnValidationException(){
        assertThrows(ValidationException.class, () -> {
            questService.createQuest(new Quest(0, "UnitQuest", "This Quest was created by a Unit Test", 100, 10, Duration.parse("PT0M"), 0, -10, 1));
        });
    }

    @Test
    @DisplayName("Inserting a quest with a repetition cycle of a week should return a new quest ith a repetition cycle of a week")
     public void insertRepetitionCycleWeek_shouldReturnRepetitionCycleWeek(){
        Quest quest = questService.createQuest(new Quest(0, "UnitQuest", "This Quest was created by a Unit Test", 100, 10, Duration.parse("PT168H"), 0, 10, 1));
        assertEquals(Duration.parse("P7D"), quest.getRepetition_cycle());
    }
}
