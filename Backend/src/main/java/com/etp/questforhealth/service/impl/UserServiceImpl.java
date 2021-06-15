package com.etp.questforhealth.service.impl;

import com.etp.questforhealth.entity.CharacterLevel;
import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.entity.StoryChapter;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.CharacterLevelDao;
import com.etp.questforhealth.persistence.UserDao;
import com.etp.questforhealth.service.EquipmentService;
import com.etp.questforhealth.service.StoryChapterService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserDao userDao;
    private final CharacterLevelDao characterLevelDao;
    private final Validator validator;
    private final EquipmentService equipmentService;
    private final StoryChapterService storyService;

    @Autowired
    public UserServiceImpl(UserDao userDao, Validator validator, CharacterLevelDao characterLevelDao, EquipmentService equipmentService, StoryChapterService storyService) {
        this.userDao = userDao;
        this.validator = validator;
        this.characterLevelDao = characterLevelDao;
        this.equipmentService = equipmentService;
        this.storyService = storyService;
    }


    @Override
    public List<User> getAll() {
        LOGGER.trace("getAll()");
        return userDao.getAll();
    }

    @Override
    public List<User> getAllUsersFromDoctor(int doctor){
        LOGGER.trace("getAllUsersFromDoctor({})", doctor);
        if (doctor < 0) {
            throw new ValidationException("Id has to be greater than 0");
        }
        return userDao.getAllUsersFromDoctor(doctor);
    }

    @Override
    public List<User> getAllNotUsersFromDoctor(int doctor){
        LOGGER.trace("getAllNotUsersFromDoctor({})", doctor);
        if (doctor < 0) {
            throw new ValidationException("Id has to be greater than 0");
        }
        return userDao.getAllNotUsersFromDoctor(doctor);
    }

    @Override
    public User getOneById(int id){
        LOGGER.trace("getOneById({})", id);
        if (id < 0) {
            throw new ValidationException("Id has to be greater than 0");
        }
        try {
            return userDao.getOneById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public int getUserStrength(int id){
        LOGGER.trace("getUserStrength({})", id);
        if (id < 0) {
            throw new ValidationException("Id has to be greater than 0");
        }
        try{
            User u = userDao.getOneById(id);
            List<Equipment> equipments = equipmentService.getWornEquipmentFromUserId(id);
            int st = u.getCharacterStrength();
            for (Equipment e: equipments) {
                st += e.getStrength();
            }
            return st;
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
        validator.validateExistingUser(user.getId());
        validator.validateExistingQuest(quest.getId());
        try{
            userDao.completeQuest(user.getId(), quest.getId(), true, quest.getDueDate() != null ? quest.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): LocalDate.now());
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
        validator.validateExistingUser(user.getId());
        try{
            int goldPenalty = 0;
            int expPenalty = 0;
            for(Quest quest: missedQuests){
                //Check for overflow
                if((goldPenalty + quest.getGold_penalty())>0) goldPenalty +=quest.getGold_penalty();
                else goldPenalty = Integer.MAX_VALUE;
                if((expPenalty + quest.getExp_penalty())>0) expPenalty +=quest.getExp_penalty();
                else expPenalty = Integer.MAX_VALUE;
                userDao.completeQuest(user.getId(), quest.getId(), false, LocalDate.now().minusDays(1));
            }
            //user can't have debt
            if (goldPenalty > user.getCharacterGold()) goldPenalty = user.getCharacterGold();
            if (expPenalty > user.getCharacterExp()) expPenalty = user.getCharacterExp();
            CharacterLevel currentLevel = characterLevelDao.getCharacterLevelById(user.getCharacterLevel());
            User updatedUser = userDao.changeUserGoldAndExp(user, -expPenalty,-goldPenalty);
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
        user.setCharacterGold(0);
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

    @Override
    public boolean changeUserGold(int id, int changeValue) {
        LOGGER.trace("changeUserGold({}, {})", id, changeValue);
        try{
            validator.validateUserGold(id, changeValue);
            return userDao.changeUserGold(id, changeValue);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean setNextStoryChapter(User user) {
        LOGGER.trace("setNetStoryChapter({})", user);
        try{
            User u = userDao.getOneById(user.getId());
            validator.validateNextStoryChapter(u);
            StoryChapter sc = storyService.getNextChapter(u);
            u.setStoryChapter(sc.getId());
            return userDao.updateUser(u);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean checkUserForNextStoryAndUpdate(User user){
        LOGGER.trace("checkUserForNextStoryAndUpdate({})", user);
        try{
            User u = userDao.getOneById(user.getId());
            try {
                validator.validateNextStoryChapter(u);
            } catch (ValidationException e) {
                return false;
            }
            StoryChapter sc = storyService.getNextChapter(u);
            u.setStoryChapter(sc.getId());
            return userDao.updateUser(u);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<User> getLeaderboardForUser(User user) {
        LOGGER.trace("getLeaderboardForUser({})", user);
        try{
            validator.validateExistingUser(user.getId());
            List<User> users = userDao.getLeaderboardForUser(user);
            for (User u: users) {
                u.setCharacterStrength(getUserStrength(u.getId()));
            }
            users.sort(Comparator.comparing(User::getCharacterStrength)
                    .thenComparing(User::getCharacterExp)
                    .thenComparing(User::getIdForSort).reversed());
            return users;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
