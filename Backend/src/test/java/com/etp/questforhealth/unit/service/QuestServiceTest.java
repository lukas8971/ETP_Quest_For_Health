package com.etp.questforhealth.unit.service;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.CreateDoctorQuest;
import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.QuestDao;
import com.etp.questforhealth.service.DoctorService;
import com.etp.questforhealth.service.QuestService;
import com.etp.questforhealth.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class QuestServiceTest {

    @Autowired
    QuestService questService;
    @Autowired
    QuestDao questDao;
    @Autowired
    UserService userService;
    @Autowired
    DoctorService doctorService;

    @BeforeAll
    public static void testData(){
        DatabaseTestData.insertTestData();
    }

    @Test
    @DisplayName("Requesting a not existing quest should throw NotFoundException")
    public void requestingNotExistingQuest_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            questService.getOneById(99999);
        });
    }


    @Test
    @DisplayName("Inserting a valid quest should return the quest with a positive id.")
    public void insertValidQuest_shouldReturnPositiveId() {
        Quest quest = questService.createQuest(TestData.getNewCreateDoctorQuest());
        assertTrue(quest.getId() > 0);
    }

    @Test
    @DisplayName("Inserting invalid characters in the name should return a ValidationException")
    public void insertInvalidName_shouldReturnValidationException() {
        assertThrows(ValidationException.class, () -> {
            CreateDoctorQuest cdq = TestData.getNewCreateDoctorQuest();
            cdq.getQuest().setName("UnitQuest’;");
            questService.createQuest(cdq);
        });

    }

    @Test
    @DisplayName("Inserting invalid characters in the description should return a ValidationException")
    public void insertInvalidDescription_shouldReturnValidationException() {
        assertThrows(ValidationException.class, () -> {
            CreateDoctorQuest cdq = TestData.getNewCreateDoctorQuest();
            cdq.getQuest().setDescription("UnitQuest’;");
            questService.createQuest(cdq);
        });
    }

    @Test
    @DisplayName("Inserting a quest with a negative repetition cycle should return a ValidationException")
    public void insertNegativeDuration_shouldReturnValidationException() {
        assertThrows(ValidationException.class, () -> {
            CreateDoctorQuest cdq = TestData.getNewCreateDoctorQuest();
            cdq.getQuest().setRepetition_cycle(Duration.parse("-PT10H"));
            questService.createQuest(cdq);
        });
    }

    @Test
    @DisplayName("Inserting a quest with a negative EXP reward should return a ValidationException")
    public void insertNegativeExpReward_shouldReturnValidationException() {
        assertThrows(ValidationException.class, () -> {
            CreateDoctorQuest cdq = TestData.getNewCreateDoctorQuest();
            cdq.getQuest().setExp_reward(-100);
            questService.createQuest(cdq);
        });
    }

    @Test
    @DisplayName("Inserting a quest with a negative gold reward should return a ValidationException")
    public void insertNegativeGoldReward_shouldReturnValidationException() {
        assertThrows(ValidationException.class, () -> {
            CreateDoctorQuest cdq = TestData.getNewCreateDoctorQuest();
            cdq.getQuest().setGold_reward(-10);
            questService.createQuest(cdq);
        });
    }

    @Test
    @DisplayName("Inserting a quest with a negative EXP penalty should return a ValidationException")
    public void insertNegativeEXPPenalty_shouldReturnValidationException() {
        assertThrows(ValidationException.class, () -> {
            CreateDoctorQuest cdq = TestData.getNewCreateDoctorQuest();
            cdq.getQuest().setExp_penalty(-100);
            questService.createQuest(cdq);
        });
    }


    @Test
    @DisplayName("Inserting a quest with a negative gold penalty should return a ValidationException")
    public void insertNegativeGoldPenalty_shouldReturnValidationException() {
        assertThrows(ValidationException.class, () -> {
            CreateDoctorQuest cdq = TestData.getNewCreateDoctorQuest();
            cdq.getQuest().setGold_penalty(-10);
            questService.createQuest(cdq);
        });
    }

    @Test
    @DisplayName("Inserting a quest with a repetition cycle of a week should return a new quest ith a repetition cycle of a week")
    public void insertRepetitionCycleWeek_shouldReturnRepetitionCycleWeek() {
        CreateDoctorQuest cdq = TestData.getNewCreateDoctorQuest();
        cdq.getQuest().setRepetition_cycle(Duration.parse("PT168H"));
        Quest quest = questService.createQuest(cdq);
        assertEquals(Duration.parse("P7D"), quest.getRepetition_cycle());
        assertEquals(Duration.parse("P7D"), quest.getRepetition_cycle());
    }

    @Test
    @DisplayName("Having a doctor user relationship and assigning quests to that user should not throw anything")
    public void assigningQuest_doctorUserRelationship_shouldNotThrow() {
        User u = TestData.getNewWorkingUser();
        Doctor d = TestData.getExistingDoctorTestDoc1();
        User savedUser = userService.createUser(u);
        assertNotNull(savedUser);
        assertTrue(doctorService.assignNewPatient(d.getId(), savedUser.getId()));
        AcceptedQuest acceptedQuest = new AcceptedQuest(7, savedUser.getId(), LocalDate.now());
        assertTrue(questService.addAssignedDoctorQuestForUser(acceptedQuest));
    }

    @Test
    @DisplayName("Adding a doctor quest for a user that is not in treatment at a doctor should throw ValidationException")
    public void assigningDoctorQuest_noRelatinoship_shouldThrowValidationException() {
        User u = TestData.getNewWorkingUserDifferentCharacter();
        Doctor d = TestData.getExistingDoctorTestDoc1();
        User savedUser = userService.createUser(u);
        assertNotNull(savedUser);
        AcceptedQuest acceptedQuest = new AcceptedQuest(7, savedUser.getId(), LocalDate.now());
        assertThrows(ValidationException.class, () -> questService.addAssignedDoctorQuestForUser(acceptedQuest));
    }

    @Test
    @DisplayName("Adding the same doctor quest to a user twice should throw ValidationException")
    public void addingSameDocotorQuestTwice_sameUser_shouldThrowValidationException(){
        User u = TestData.getNewWorkingUserDifferentCharacter2();
        Doctor d = TestData.getExistingDoctorGeraldSteinhardt2();
        User savedUser = userService.createUser(u);
        assertNotNull(savedUser);
        assertTrue(doctorService.assignNewPatient(d.getId(), savedUser.getId()));
        AcceptedQuest acceptedQuest = new AcceptedQuest(4, savedUser.getId(), LocalDate.now());
        assertDoesNotThrow(() -> questService.addAssignedDoctorQuestForUser(acceptedQuest));
        assertThrows(ValidationException.class, () -> questService.addAssignedDoctorQuestForUser(acceptedQuest));
    }

    @Test
    @DisplayName("Removig not assigned quest from user should throw ValidationException")
    public void removingNotAssignedDoctorQuest_shouldThrowValidationException() {
        User u = TestData.getNewWorkingUserDifferentCharacter3();
        Doctor d = TestData.getExistingDoctorGeraldSteinhardt2();
        User savedUser = userService.createUser(u);
        assertNotNull(savedUser);
        assertTrue(doctorService.assignNewPatient(d.getId(), savedUser.getId()));
        assertThrows(ValidationException.class, () -> questService.deleteAssignedDoctorQuestForUser(4, savedUser.getId()));
    }
}
