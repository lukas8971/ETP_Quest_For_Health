package com.etp.questforhealth.persistence;


import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;
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
    Quest getOneById(int id) throws NotFoundException;

   /**
    * User accepts a quest
    * @param userId id of the user
    * @param questId id of the quest
    */
     boolean acceptQuest(int userId, int questId);

   /**
    * Returns all normal quests the user has not yet accepted
    * @param userId the id of the user for which to find new quests
    * @return a List of new quests for the user
    */
    List<Quest> getNewQuestsForUserId (int userId);
   

   /**
    * Saves the Quest in the persistence.
    *
    * @param quest The quest to be saved.
    * @return The saved quest with an id.
    */
   Quest createQuest(Quest quest);


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

   /**
    * Checks if an accepted quest is already accepted by a user.
    * @param acceptedQuest AcceptedQuest object that should be checked.
    * @return FALSE if either: no entry in the persistent data store, quest has no repetition cycle AND is already completed, else
    * TRUE if either: quest is already accepted by the user AND not completed, quest is completed AND has a repetition cycle AND the repetition cycle is not already met
    */
   boolean checkIfQuestAlreadyAccepted(AcceptedQuest acceptedQuest);

}
