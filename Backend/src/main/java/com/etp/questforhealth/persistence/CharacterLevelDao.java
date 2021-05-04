package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.CharacterLevel;

public interface CharacterLevelDao {
    CharacterLevel getCharacterLevelById(int id);

    CharacterLevel getCharacterLevelByLevel(int id);

    /**
     * Gets the character level a user would have if he has the provided exp
     * @param exp the exp
     * @return the CharacterLevel
     */
    CharacterLevel getCharacterLevelByExp (int exp);

    void rollbackChanges();
}
