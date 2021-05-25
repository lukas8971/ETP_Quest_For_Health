package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.CharacterLevel;

public interface CharacterLevelService {
    CharacterLevel getCharacterLevelById(int id);

    CharacterLevel getCharacterLevelByLevel(int id);

    /**
     * Gets the next level of a level
     * @param id of the current level
     * @return the next level
     */
    CharacterLevel getCharacterNextLevel(int id);
}
