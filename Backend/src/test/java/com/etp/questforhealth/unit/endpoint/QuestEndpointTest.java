package com.etp.questforhealth.unit.endpoint;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.endpoint.QuestEndpoint;
import com.etp.questforhealth.endpoint.dto.CreateDoctorQuestDto;
import com.etp.questforhealth.endpoint.dto.CredentialsDto;
import com.etp.questforhealth.endpoint.dto.QuestDto;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.persistence.QuestDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    public static void testData(){
        DatabaseTestData.insertTestData();
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
        QuestDto questDto = questEndpoint.createQuest(TestData.getNewCreateDoctorQuestDto());
        assertTrue(questDto.getId() > 0);
    }

    @Test
    @DisplayName("Inserting invalid characters in the name should return an UNPROCESSABLE_ENTITY Status")
    public void insertInvalidName_shouldReturnUnprocessable_EntityStatus() {
        try {
            CreateDoctorQuestDto cdq = TestData.getNewCreateDoctorQuestDto();
            cdq.getQuest().setName("UnitQuest’;");
            questEndpoint.createQuest(cdq);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }

    @Test
    @DisplayName("Inserting invalid characters in the description should return an UNPROCESSABLE_ENTITY Status")
    public void insertInvalidDescription_shouldReturnUnprocessable_EntityStatus() {
        try {
            CreateDoctorQuestDto cdq = TestData.getNewCreateDoctorQuestDto();
            cdq.getQuest().setDescription("UnitQuest’;");
            questEndpoint.createQuest(cdq);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }

    @Test
    @DisplayName("Inserting a quest with a negative repetition cycle should return an UNPROCESSABLE_ENTITY Status")
    public void insertNegativeDuration_shouldReturnUnprocessable_EntityStatus(){
        try {
            CreateDoctorQuestDto cdq = TestData.getNewCreateDoctorQuestDto();
            cdq.getQuest().setRepetition_cycle(Duration.parse("-PT12H"));
            questEndpoint.createQuest(cdq);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }

    @Test
    @DisplayName("Inserting a quest with a negative EXP reward should return an UNPROCESSABLE_ENTITY Status")
    public void insertNegativeExpReward_shouldReturnUnprocessable_EntityStatus(){
        try {
            CreateDoctorQuestDto cdq = TestData.getNewCreateDoctorQuestDto();
            cdq.getQuest().setExp_reward(-100);
            questEndpoint.createQuest(cdq);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }

    @Test
    @DisplayName("Inserting a quest with a negative gold reward should return an UNPROCESSABLE_ENTITY Status")
    public void insertNegativeGoldReward_shouldReturnUnprocessable_EntityStatus(){
        try {
            CreateDoctorQuestDto cdq = TestData.getNewCreateDoctorQuestDto();
            cdq.getQuest().setGold_reward(-10);
            questEndpoint.createQuest(cdq);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }
    @Test
    @DisplayName("Inserting a quest with a negative EXP penalty should return an UNPROCESSABLE_ENTITY Status")
    public void insertNegativeEXPPenalty_shouldReturnUnprocessable_EntityStatus(){
        try {
            CreateDoctorQuestDto cdq = TestData.getNewCreateDoctorQuestDto();
            cdq.getQuest().setExp_penalty(-10);
            questEndpoint.createQuest(cdq);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }


    @Test
    @DisplayName("Inserting a quest with a negative gold penalty should return an UNPROCESSABLE_ENTITY Status")
    public void insertNegativeGoldPenalty_shouldReturnUnprocessable_EntityStatus(){
        try {
            CreateDoctorQuestDto cdq = TestData.getNewCreateDoctorQuestDto();
            cdq.getQuest().setGold_penalty(-10);
            questEndpoint.createQuest(cdq);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
        }
    }

    @Test
    @DisplayName("Inserting a quest with a repetition cycle of a week should return a new quest ith a repetition cycle of a week")
    public void insertRepetitionCycleWeek_shouldReturnRepetitionCycleWeek() {
        try {
            CreateDoctorQuestDto cdq = TestData.getNewCreateDoctorQuestDto();
            cdq.getQuest().setRepetition_cycle(Duration.parse("PT168H"));
            QuestDto questDto = questEndpoint.createQuest(cdq);
            assertEquals(Duration.parse("P7D"), questDto.getRepetition_cycle());

        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.getStatus());
            fail();
        }
    }

}
