package com.etp.questforhealth.util;


import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.DoctorDao;
import com.etp.questforhealth.persistence.QuestDao;
import com.etp.questforhealth.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final QuestDao questDao;
    private final DoctorDao doctorDao;
    private final UserDao userDao;

    @Autowired
    public Validator(QuestDao questDao, DoctorDao doctorDao, UserDao userDao){
        this.questDao = questDao;
        this.doctorDao = doctorDao;
        this.userDao = userDao;
    }
  
      public void validateNewUser (User user){
        LOGGER.trace("validateNewUser({})", user.toString());
        String errorMsg = "";
        if(user.getFirstname() == null || user.getFirstname().isBlank()){
            errorMsg += "You must enter a firstname. \n";
        }
        if(user.getLastname() == null || user.getLastname().isBlank()){
            errorMsg += "You must enter a lastname. \n";
        }
        if(user.getCharacterName() == null || user.getCharacterName().isBlank()){
            errorMsg += "You must enter a character-name. \n";
        }
        if(user.getPassword() == null || user.getPassword().isBlank()){
            errorMsg += "You must enter a password. \n";
        }
        if(!errorMsg.isBlank()){
            throw new ValidationException(errorMsg);
        }
    }

    /**
     * Checks if a doctor user relationship is given
     * @param doctor id of the doctor
     * @param user id of the user
     */
    public void validateDoctorUserRelation(int doctor, int user){
        LOGGER.trace("validateDoctorUserRelation({}, {})", doctor, user);
        validateExistingDoctor(doctor);
        validateExistingUser(user);
        if (!doctorDao.checkIfDoctorUserRelationshipExists(doctor, user)){
            throw new ValidationException("User " + user + " is not under treatment of doctor " + doctor);
        }
    }

    /**
     * Checks if an AcceptedQuest is valid an not already accepted by a user
     * @param acceptedQuest to be added to a user
     */
    public void validateAcceptedQuest(AcceptedQuest acceptedQuest){
        LOGGER.trace("validateAcceptedQuest({})", acceptedQuest);
        validateExistingQuest(acceptedQuest.getQuest());
        validateExistingUser(acceptedQuest.getUser());
        if (questDao.checkIfQuestAlreadyAccepted(acceptedQuest)) throw new ValidationException("User already has this quest (accepted or in repetition)");
    }

    /**
     * Validates an existing user via its id
     * @param user id of the user to check
     */
    public void validateExistingUser(int user){
        LOGGER.trace("validateUser({})", user);
        if (user < 0) throw new IllegalArgumentException("ID of an user has to be greater than 0");
        User u = userDao.getOneById(user);
        if (u == null) throw new ValidationException("User does not exist!");
    }

    /**
     * Validates an existing doctor via its id
     * @param doctor id of the doctor to check
     */
    public void validateExistingDoctor(int doctor){
        LOGGER.trace("validateDoctor({})", doctor);
        if (doctor < 0) throw new IllegalArgumentException("ID of a doctor has to be greater than 0");
        Doctor d = doctorDao.getOneById(doctor);
        if (d == null) throw new ValidationException("Doctor does not exist!");
    }

    /**
     * Validates an existing quest via its id
     * @param quest id of the quest to validate
     */
    public void validateExistingQuest(int quest){
        LOGGER.trace("validateQuest({})", quest);
        if (quest < 0) throw new IllegalArgumentException("ID of a Quest has to be greater than 0");
        Quest q = questDao.getOneById(quest);
        if (q == null) throw new ValidationException("Quest does not exist!");
    }
}
