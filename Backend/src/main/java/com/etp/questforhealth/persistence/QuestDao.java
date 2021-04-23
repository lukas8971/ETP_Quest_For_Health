package com.etp.questforhealth.persistence;


import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestDao {

   /**
    *  Returns the quest of the given id.
    * @param id the id of the quest
    * @return a Quest entity with the given id
    * @throws NotFoundException if the id was not found in the persistence
    */
   public Quest getOneById(int id) throws NotFoundException;

   /**
    * Returns all the available doctor quests for a user
    * @param user to assign quests to
    * @param doctor that assigns the quests to a user
    * @return a list of all available quests from a doctor to a user
    * @throws PersistenceException if something went wrong.
    */
   List<Quest> getAllUserAvailableDoctorQuests(int user, int doctor);

   /**
    * Returns all the assigned doctor quests for a user
    * @param user to assign quests to
    * @param doctor that assigns the quests to a user
    * @return a list of all assigned quests from a doctor to a user
    * @throws PersistenceException if something went wrong.
    */
   List<Quest> getAllUserAssignedDoctorQuests(int user, int doctor);

   /**
    * Deletes an already assigned doctor quest for a patient
    * @param quest the id of the quest to delete
    * @param user the patient that has the quest assigned to
    * @return true if delete was successful
    * @throws PersistenceException if something went wrong.
    */
   boolean deleteAssignedDoctorQuestForUser(int quest, int user);

   /**
    * Adds a new assigned doctor quest for a patient
    * @param acceptedQuest the quest that should be added
    * @return true if successfully added to the users assigned quests
    * @throws PersistenceException if something went wrong.
    */
   boolean addAssignedDoctorQuestForUser(AcceptedQuest acceptedQuest);
}
