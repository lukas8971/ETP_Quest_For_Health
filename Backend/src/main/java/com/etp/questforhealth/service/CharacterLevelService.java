package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.CharacterLevel;

public interface CharacterLevelService {
    CharacterLevel getCharacterLevelById(int id);

    CharacterLevel getCharacterLevelByLevel(int id);

    void rollbackChanges();
}
