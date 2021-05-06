package com.etp.questforhealth.unit.persistence;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.entity.CharacterLevel;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.persistence.CharacterLevelDao;
import org.junit.jupiter.api.BeforeAll;
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
public class CharacterLevelPersistenceTest {

    @Autowired
    CharacterLevelDao characterLevelDao;

    @BeforeAll
    public static void testData(){
        DatabaseTestData.insertTestData();
    }

    @Test
    @DisplayName("Requesting an existing Character-Level by id should work")
    public void getCharacterLevelById_shouldWork(){
        assertDoesNotThrow(() ->characterLevelDao.getCharacterLevelById(1));
    }

    @Test
    @DisplayName("Requesting an non-existing Character-Level by id should throw a NotFoundException")
    public void getCharacterLevelById_shouldThrowNotFoundException(){
        assertThrows(NotFoundException.class, () -> characterLevelDao.getCharacterLevelById(-1));
    }

    @Test
    @DisplayName("Requesting an existing Character-Level by level should work")
    public void getCharacterLevelByLevel_shouldWork(){
        assertDoesNotThrow(() ->characterLevelDao.getCharacterLevelByLevel(1));
    }

    @Test
    @DisplayName("Requesting an non-existing Character-Level level should throw a NotFoundException")
    public void getCharacterLevelByLevel_shouldThrowNotFoundException(){
        assertThrows(NotFoundException.class, () -> characterLevelDao.getCharacterLevelByLevel(-1));
    }

    @Test
    @DisplayName("Requesting an existing Character-Level which requires 0 or less exp should work")
    public void getCharacterLevelByExp_shouldWork() {
        CharacterLevel level = characterLevelDao.getCharacterLevelByExp(0);
        assertTrue (level != null);
    }

    @Test
    @DisplayName("Requesting a Character-Level which requires negative exp should throw a NullPointerException")
    public void getCharacterLevelByExp_shouldThrowNotFoundException(){
        assertThrows(NotFoundException.class, () -> characterLevelDao.getCharacterLevelByExp(-1));
    }

    @Test
    @DisplayName("Requesting one Character-Level which requires 0 and one which requires 100000 or less exp should return different levels")
    public void getCharacterLevelByExp_shouldBeDifferent() {
        CharacterLevel level1 = characterLevelDao.getCharacterLevelByExp(0);
        CharacterLevel level2 = characterLevelDao.getCharacterLevelByExp(100000);
        assertTrue (!(level1.equals(level2)));
    }
}
