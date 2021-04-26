package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.CharacterLevel;

public interface CharacterLevelDao {
    CharacterLevel getCharacterLevelById(int id);

    CharacterLevel getCharacterLevelByLevel(int id);

    void rollbackChanges();
}
