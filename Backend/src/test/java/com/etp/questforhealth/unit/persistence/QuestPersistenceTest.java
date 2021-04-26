package com.etp.questforhealth.unit.persistence;

import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.persistence.QuestDao;
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
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class QuestPersistenceTest {

    @Autowired
    QuestDao questDao;


    @AfterEach
    public void tearDownDBData(){
        questDao.rollbackChanges();
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

}
