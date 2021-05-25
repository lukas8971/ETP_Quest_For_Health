package com.etp.questforhealth.service.impl;

import com.etp.questforhealth.entity.CharacterLevel;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.persistence.CharacterLevelDao;
import com.etp.questforhealth.service.CharacterLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class CharacterLevelServiceImpl implements CharacterLevelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CharacterLevelDao characterLevelDao;

    @Autowired
    public CharacterLevelServiceImpl(CharacterLevelDao characterLevelDao) {
        this.characterLevelDao = characterLevelDao;
    }

    @Override
    public CharacterLevel getCharacterLevelById(int id) {
        try {
            return characterLevelDao.getCharacterLevelById(id);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public CharacterLevel getCharacterLevelByLevel(int id) {
        try {
            return characterLevelDao.getCharacterLevelByLevel(id);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public CharacterLevel getCharacterNextLevel(int id) {
        LOGGER.trace("getCharacterNextLevel({})", id);
        try {
            return characterLevelDao.getCharacterLevelByLevel(id + 1);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }
}
