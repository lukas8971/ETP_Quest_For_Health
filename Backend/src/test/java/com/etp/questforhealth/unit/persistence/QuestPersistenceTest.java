package com.etp.questforhealth.unit.persistence;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.persistence.QuestDao;
import com.etp.questforhealth.persistence.UserDao;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class QuestPersistenceTest {

    @Autowired
    QuestDao questDao;

    @Autowired
    UserDao userDao;

    @BeforeEach
    public void testData(){
        DatabaseTestData.insertTestData();
    }

    @Test
    @DisplayName("Requesting a not existing quest should throw NotFoundException")
    public void requestingNotExistingQuest_shouldThrowNotFoundException(){
        assertThrows(NotFoundException.class, () -> {
            questDao.getOneById(99999);
        });
    }

    @Test
    @DisplayName("Requesting a quest that does exist should return the quest")
    public void requestingExistingQuest_shouldReturnQuest() {
        try{
            assertTrue(questDao.getOneById(1).getId() == 1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Inserting a quest with a repetition cycle of a week should return a new quest ith a repetition cycle of a week")
    public void insertRepetitionCycleWeek_shouldReturnRepetitionCycleWeek(){
        Quest quest = questDao.createQuest(new Quest(0, "UnitQuest", "This Quest was created by a Unit Test", 100, 10, Duration.parse("PT168H"), 0, 10, 1, new Date()));
        assertEquals(Duration.parse("P7D"), quest.getRepetition_cycle());
    }

    @Test
    @DisplayName("Accepting an existing Quest with an existing user should work")
    public void acceptQuest_shouldWork(){
        questDao.acceptQuest(1,1);
        assertAll(
                ()-> assertEquals(1, questDao.getAllAcceptedQuestForUser(1).size()),
                ()-> assertEquals(1, questDao.getAllAcceptedQuestForUser(1).get(0).getQuest())
        );
    }

    @Test
    @DisplayName("Accepting an existing Quest with a non-existing user should throw a PersistenceException")
    public void acceptQuest_noValidUser_shouldThrowPersistenceException(){
        assertThrows(PersistenceException.class, () -> questDao.acceptQuest(-1,1));
    }

    @Test
    @DisplayName("Accepting a non-existing Quest with a non-existing user should throw a PersistenceException")
    public void acceptQuest_noValidQuest_shouldThrowPersistenceException(){
        assertThrows(PersistenceException.class, () -> questDao.acceptQuest(1,-1));
    }

    @Test
    @DisplayName("Getting all new Quests for user 1 should return all quests")
    public void getNewQuestsForUserId_shouldWork(){
        List<Quest> quests = questDao.getNewQuestsForUserId(1);
        assertTrue (quests.size() > 0);
    }

    @Test
    @DisplayName("Getting all new Quests for non-existing user should return all quests")
    public void getNewQuestsForUserId_invalidUser_shouldBeEmpty(){
        assertFalse( questDao.getNewQuestsForUserId(-1).isEmpty());
    }

    @Test
    @DisplayName("Accepting a quest should remove it from the list of new quests for a user")
    public void getNewQuestsForUserId_acceptQuest_shouldRemoveQuest(){
        List<Quest> questsBefore = questDao.getNewQuestsForUserId(1);
        questDao.acceptQuest(1,1);
        Quest acceptedQuest = questDao.getOneById(1);
        List<Quest> questsAfter = questDao.getNewQuestsForUserId(1);
        assertAll(
                () -> assertTrue(questsBefore.size() == questsAfter.size()+1),
                () -> assertFalse(questsAfter.contains(acceptedQuest))
        );
    }

    @Test
    @DisplayName("Requesting all due Quests for an invalid user should return an empty list")
    public void getAllQuestsDueForUser_invalidUser_shouldBeEmpty(){
        assertTrue(questDao.getAllQuestsDueForUser(-1).isEmpty());
    }

    @Test
    @DisplayName("Requesting all due Quests for User 1 should return 1 quest")
    public void getAllQuestsDueForUser_userAcceptedQuest_shouldWork(){
        questDao.acceptQuest(1,1);
        assertEquals(1, questDao.getAllQuestsDueForUser(1).size());
    }

    @Test
    @DisplayName("Requesting all due Quests when the user has already completed them should return an empty list")
    public void getAllQuestsDueForUser_userCompletedQuest_shouldWork(){
        questDao.acceptQuest(1,1);
        Quest quest = questDao.getOneById(1);
        userDao.completeQuest(1,1,true, LocalDate.now().plusDays(quest.getRepetition_cycle().toDays()));
        assertTrue(questDao.getAllQuestsDueForUser(1).isEmpty());
    }

    @Test
    @DisplayName("Requesting all open one-time quests for user 1 should return an empty list")
    public void getAllOpenOneTimeQuestsForUser_shouldBeEmpty(){
        assertTrue(questDao.getAllOpenOneTimeQuestsForUser(1).isEmpty());
    }

    @Test
    @DisplayName("Requesting all open one-time quests for a non-existing user should return an empty list")
    public void getAllOpenOneTimeQuestsForUser_invalidUser_shouldBeEmpty(){
        assertTrue( questDao.getAllOpenOneTimeQuestsForUser(-1).isEmpty());
    }

    @Test
    @DisplayName("Requesting all open one-time quests for a user which has accepted one Quest should work")
    public void getAllOpenOneTimeQuestsForUser_shouldWork(){
        questDao.acceptQuest(1,2);
        assertEquals(1, questDao.getAllOpenOneTimeQuestsForUser(1).size());
    }

    @Test
    @DisplayName ("Requesting all open one-time quests for a user which has completed all his quests should return an empty list")
    public void getAllOpenOneTimeQuestsForUser_noOpenQuests_shouldBeEmpty(){
        questDao.acceptQuest(1,2);
        userDao.completeQuest(1,2,true,LocalDate.now());
        assertTrue(questDao.getAllOpenOneTimeQuestsForUser(1).isEmpty());
    }

    @Test
    @DisplayName("Requesting missed quests for an invalid user should return an empty list")
    public void getMissedQuestsForUser_invalidUser_shouldBeEmpty(){
        assertTrue(questDao.getAllMissedQuestsForUser(-1).isEmpty());
    }

    @Test
    @DisplayName("Requesting missed quests for a user with no quests should return an empty list")
    public void getMissedQuestsForUser_shouldBeEmpty(){
        assertTrue (questDao.getAllMissedQuestsForUser(1).isEmpty());
    }

    @Test
    @DisplayName("Requesting missed quests for user 4 should return a list with one quest")
    public void getMissedQuestsForUser_shouldWork(){
        assertEquals(1, questDao.getAllQuestsDueForUser(4).size());
    }
}
