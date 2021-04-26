package com.etp.questforhealth.service.impl;


import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.persistence.QuestDao;
import com.etp.questforhealth.service.QuestService;
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
    private final Validator validator;

    @Autowired
    public QuestServiceImpl(QuestDao questDao, Validator validator){
        this.questDao = questDao;
        this.validator = validator;
    }

    @Override
    public Quest getOneById(int id) throws NotFoundException {
        LOGGER.trace("getOneById({})",id);
        if(id < 0) throw new NotFoundException("The Quest ID must be greater than 0.");
        return questDao.getOneById(id);
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
