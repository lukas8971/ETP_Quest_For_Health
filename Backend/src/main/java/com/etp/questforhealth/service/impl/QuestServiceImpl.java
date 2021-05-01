package com.etp.questforhealth.service.impl;


import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.CreateDoctorQuest;
import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.persistence.QuestDao;
import com.etp.questforhealth.persistence.UserDao;
import com.etp.questforhealth.service.DoctorService;
import com.etp.questforhealth.service.QuestService;

import com.etp.questforhealth.util.validator.QuestValidator;

import com.etp.questforhealth.util.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class QuestServiceImpl implements QuestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final QuestDao questDao;
    private final UserDao userDao;

    private final QuestValidator questValidator;
    private final Validator validator;
    private final DoctorService doctorService;


    @Autowired
    public QuestServiceImpl(QuestDao questDao, Validator validator, QuestValidator questValidator, DoctorService doctorService, UserDao userDao){
        this.questDao = questDao;
        this.questValidator = questValidator;
        this.validator = validator;
        this.doctorService = doctorService;
        this.userDao = userDao;

    }

    @Override
    public Quest getOneById(int id) throws NotFoundException {
        LOGGER.trace("getOneById({})",id);
        if(id < 0) throw new NotFoundException("The Quest ID must be greater than 0.");
        return questDao.getOneById(id);
    }

    @Override
    public List<Quest> getNewQuestsForUserId(int userId) {
        LOGGER.trace("getNewQuestsForUserId({})", userId);
        try{
            return questDao.getNewQuestsForUserId(userId);
        }catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public boolean acceptQuest(int userId, int questId) {
        LOGGER.trace("acceptQuest({},{})", userId,questId);
        try{
            return questDao.acceptQuest(userId,questId);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }
    @Override
    public Quest createQuest(CreateDoctorQuest createDoctorQuest) throws ValidationException {
        LOGGER.trace("createQeust({})",createDoctorQuest);
        Doctor doctor = doctorService.checkLogin(createDoctorQuest.getCredentials());
        Quest quest = createDoctorQuest.getQuest();

        // sets the doctor in the quest to the authorized doctor by the credentials
        quest.setDoctor(doctor.getId());

        questValidator.validateQuest(quest);
        return questDao.createQuest(quest);
    }

    @Override
    public List<Quest> getAllQuestsDueForUser(int userId) {
        LOGGER.trace("getAllQuestsDueForUser({})", userId);
        try{
            return questDao.getAllQuestsDueForUser(userId);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public List<Quest> getAllMissedQuestsForUser(int userId) {
        LOGGER.trace("getAllMissedQuestsForUser({})", userId);
        try{
            List<Quest> missedQuests = questDao.getAllMissedQuestsForUser(userId);
            //Insert quests into completed-table but mark them as uncompleted so that the user only gets notified once
            /*for (Quest q: missedQuests){
                userDao.completeQuest(userId,q.getId(),false);
            }

             */
            return missedQuests;
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public List<Quest> getAllOpenOneTimeQuestsForUser(int userId) {
        LOGGER.trace("getAllOpenOneTimeQuestsForUser({})", userId);
        try{
            return questDao.getAllOpenOneTimeQuestsForUser(userId);
        }catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public List<Quest> getAllUserAvailableDoctorQuests(int user, int doctor){
        LOGGER.trace("getAllUserAvailableDoctorQuests({}, {})", user, doctor);
        try {
            validator.validateDoctorUserRelation(doctor, user);
            return questDao.getAllUserAvailableDoctorQuests(user, doctor);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Quest> getAllUserAssignedDoctorQuests(int user, int doctor){
        LOGGER.trace("getAllUserAssignedDoctorQuests({}, {})", user, doctor);
        try {
            validator.validateDoctorUserRelation(doctor, user);
            return questDao.getAllUserAssignedDoctorQuests(user, doctor);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteAssignedDoctorQuestForUser(int quest, int user){
        LOGGER.trace("deleteAssignedDoctorQuestForUser({}, {})", quest, user);
        try {
            return questDao.deleteAssignedDoctorQuestForUser(quest, user);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean addAssignedDoctorQuestForUser(AcceptedQuest acceptedQuest){
        LOGGER.trace("addAssignedDoctorQuestForUser({})", acceptedQuest);
        try {
            validator.validateAcceptedQuest(acceptedQuest);
            return questDao.addAssignedDoctorQuestForUser(acceptedQuest);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
