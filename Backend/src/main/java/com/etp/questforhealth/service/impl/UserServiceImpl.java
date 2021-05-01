package com.etp.questforhealth.service.impl;

import com.etp.questforhealth.entity.CharacterLevel;
import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.CharacterLevelDao;
import com.etp.questforhealth.persistence.UserDao;
import com.etp.questforhealth.service.UserService;
import com.etp.questforhealth.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserDao userDao;
    private final CharacterLevelDao characterLevelDao;
    private final Validator validator;

    @Autowired
    public UserServiceImpl(UserDao userDao, Validator validator, CharacterLevelDao characterLevelDao) {
        this.userDao = userDao;
        this.validator = validator;
        this.characterLevelDao = characterLevelDao;
    }


    @Override
    public List<User> getAll() {
        LOGGER.trace("getAll()");
        return userDao.getAll();
    }

    @Override
    public List<User> getAllUsersFromDoctor(int doctor){
        LOGGER.trace("getAllUsersFromDoctor({})", doctor);
        return userDao.getAllUsersFromDoctor(doctor);
    }

    @Override
    public User getOneById(int id){
        LOGGER.trace("getOneById({})", id);
        try {
            return userDao.getOneById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public User checkLogin(Credentials cred) {
        LOGGER.trace("checkLogin({})", cred.toString());
        cred.setPassword(get_SHA_512_SecurePassword(cred.getPassword()));
        try{
            return userDao.checkLogin(cred);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public User completeQuest(User user, Quest quest) {
        LOGGER.trace("completeQuest({},{})", user.toString(), quest.toString());
        try{
            userDao.completeQuest(user.getId(), quest.getId(), true);
            CharacterLevel currentLevel = characterLevelDao.getCharacterLevelById(user.getCharacterLevel());
            User updatedUser = userDao.changeUserGoldAndExp(user, quest.getExp_reward(), quest.getGold_reward());
            CharacterLevel nextLevel = characterLevelDao.getCharacterLevelByExp(updatedUser.getCharacterExp());
            if(!(currentLevel.equals(nextLevel))){
                updatedUser.setCharacterStrength(updatedUser.getCharacterStrength() + (nextLevel.getTotal_strength() - currentLevel.getTotal_strength()));
                updatedUser.setCharacterLevel(nextLevel.getId());
                userDao.updateUser(user);
            }
            return updatedUser;
        }catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public User dismissMissedQuests(User user, List<Quest> missedQuests) {
        LOGGER.trace("dismissMissedQuests({},{})", user.toString(), missedQuests.toString());
        try{
            int goldPenalty = 0;
            int expPenalty = 0;
            for(Quest quest: missedQuests){
                goldPenalty +=quest.getGold_penalty();
                expPenalty +=quest.getExp_penalty();
                userDao.completeQuest(user.getId(), quest.getId(), false);
            }
            //user can't have debt
            if (goldPenalty > user.getCharacterGold()) goldPenalty = user.getCharacterGold();
            if (expPenalty > user.getCharacterExp()) expPenalty = user.getCharacterExp();
            CharacterLevel currentLevel = characterLevelDao.getCharacterLevelById(user.getCharacterLevel());
            User updatedUser = userDao.changeUserGoldAndExp(user, -goldPenalty,-expPenalty);
            CharacterLevel newLevel = characterLevelDao.getCharacterLevelByExp(updatedUser.getCharacterExp());
            if(!(currentLevel.equals(newLevel))){
                //set user-Level
                updatedUser.setCharacterLevel(characterLevelDao.getCharacterLevelByExp(updatedUser.getCharacterExp()).getId());
                updatedUser.setCharacterStrength(updatedUser.getCharacterStrength() + ( newLevel.getTotal_strength() - currentLevel.getTotal_strength()));
                userDao.updateUser(user);
            }

            return updatedUser;
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public User createUser(User user) throws ServiceException {
        LOGGER.trace("createUser()");
        validator.validateNewUser(user);
        user.setCharacterStrength(0);
        user.setCharacterLevel(1);
        user.setCharacterExp(0);
        user.setStoryChapter(1);
        user.setPassword(get_SHA_512_SecurePassword(user.getPassword()));
        try {
            if(userDao.checkUserNameExists(user.getCharacterName())) throw new ValidationException("Character-name is already in use!");
            return userDao.createUser(user);
        }
        catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public void rollbackChanges() {
        LOGGER.trace("rollbackChanges()");
        userDao.rollbackChanges();
    }

    private static String get_SHA_512_SecurePassword(String passwordToHash){
        LOGGER.trace("get_SHA_512_SecurePassword({})", passwordToHash);
        String generatedPassword = null;
        String salt = "ABCDEFGHIJ"; //We dont use salt in our application but SHA-512 needs it so here ya go
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
