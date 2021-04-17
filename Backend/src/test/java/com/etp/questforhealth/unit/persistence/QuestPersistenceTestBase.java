package com.etp.questforhealth.unit.persistence;

import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.persistence.QuestDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public abstract class QuestPersistenceTestBase {

    @Autowired
    QuestDao questDao;

    @Test
    @DisplayName("Requesting a not existing quest should throw NotFoundException")
    public void requestingNotExistingQuest_shouldThrowNotFoundException(){
        assertThrows(NotFoundException.class, () -> {
            questDao.getOneById(32);
        });
    }
}
