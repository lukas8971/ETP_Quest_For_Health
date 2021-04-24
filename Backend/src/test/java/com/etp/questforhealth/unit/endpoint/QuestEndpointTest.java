package com.etp.questforhealth.unit.endpoint;

import com.etp.questforhealth.endpoint.QuestEndpoint;
import com.etp.questforhealth.endpoint.dto.QuestDto;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.persistence.QuestDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class QuestEndpointTest {

    @Autowired
    QuestEndpoint questEndpoint;
    @Autowired
    QuestDao questDao;

    @AfterEach
    public void tearDownDBData(){
        questDao.rollbackChanges();
    }

    @Test
    @DisplayName("Requesting a not existing quest should throw ResponseStatusException")
    public void requestingNotExistingQuest_shouldResponseStatusException(){
        assertThrows(ResponseStatusException.class, () -> {
            questEndpoint.getOneById(999999);
        });
    }

    @Test
    @DisplayName("Inserting a valid quest should return the quest with a positive id.")
    public void insertValidQuest_shouldReturnPositiveId(){
        QuestDto questDto = questEndpoint.createQuest(new QuestDto(0,"UnitQuest","This Quest was created by a Unit Test", 100,10, Duration.parse("PT0S"),0,0,1));
        assertTrue(questDto.getId() > 0);
    }

    @Test
    @DisplayName("Inserting invalid characters in the name should return an UNPROCESSABLE_ENTITY Status")
    public void insertInvalidName_shouldReturnUnprocessable_EntityStatus() {
        try {
            questEndpoint.createQuest(new QuestDto(0, "UnitQuest’;", "This Quest was created by a Unit Test", 100, 10, Duration.parse("PT0S"), 0, 0, 1));
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }

    @Test
    @DisplayName("Inserting invalid characters in the description should return an UNPROCESSABLE_ENTITY Status")
    public void insertInvalidDescription_shouldReturnUnprocessable_EntityStatus() {
        try {
            questEndpoint.createQuest(new QuestDto(0, "UnitQuest’;", "This Quest was :created by a Unit Test", 100, 10, Duration.parse("PT0S"), 0, 0, 1));
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }

    @Test
    @DisplayName("Inserting a quest with a negative repetition cycle should return an UNPROCESSABLE_ENTITY Status")
    public void insertNegativeDuration_shouldReturnUnprocessable_EntityStatus(){
        try {
            questEndpoint.createQuest(new QuestDto(0, "UnitQuest", "This Quest was created by a Unit Test", 100, 10, Duration.parse("-PT10M"), 0, 0, 1));
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }

    @Test
    @DisplayName("Inserting a quest with a negative EXP reward should return an UNPROCESSABLE_ENTITY Status")
    public void insertNegativeExpReward_shouldReturnUnprocessable_EntityStatus(){
        try {
            questEndpoint.createQuest(new QuestDto(0, "UnitQuest", "This Quest was created by a Unit Test", -100, 10, Duration.parse("PT0M"), 0, 0, 1));
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }

    @Test
    @DisplayName("Inserting a quest with a negative gold reward should return an UNPROCESSABLE_ENTITY Status")
    public void insertNegativeGoldReward_shouldReturnUnprocessable_EntityStatus(){
        try {
            questEndpoint.createQuest(new QuestDto(0, "UnitQuest", "This Quest was created by a Unit Test", 100, -10, Duration.parse("PT0M"), 0, 0, 1));
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }
    @Test
    @DisplayName("Inserting a quest with a negative EXP penalty should return an UNPROCESSABLE_ENTITY Status")
    public void insertNegativeEXPPenalty_shouldReturnUnprocessable_EntityStatus(){
        try {
            questEndpoint.createQuest(new QuestDto(0, "UnitQuest", "This Quest was created by a Unit Test", 100, 10, Duration.parse("PT0M"), -10, 0, 1));
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }


    @Test
    @DisplayName("Inserting a quest with a negative gold penalty should return an UNPROCESSABLE_ENTITY Status")
    public void insertNegativeGoldPenalty_shouldReturnUnprocessable_EntityStatus(){
        try {
            questEndpoint.createQuest(new QuestDto(0, "UnitQuest", "This Quest was created by a Unit Test", 100, 10, Duration.parse("PT0M"), 0, -10, 1));
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }

    @Test
    @DisplayName("Inserting a quest with a repetition cycle of a week should return a new quest ith a repetition cycle of a week")
    public void insertRepetitionCycleWeek_shouldReturnRepetitionCycleWeek() {
        try {
            QuestDto questDto = questEndpoint.createQuest(new QuestDto(0, "UnitQuest", "This Quest was created by a Unit Test", 100, 10, Duration.parse("PT168H"), 0, 10, 1));
            assertEquals(Duration.parse("P7D"), questDto.getRepetition_cycle());

        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
            fail();
        }
    }

}
