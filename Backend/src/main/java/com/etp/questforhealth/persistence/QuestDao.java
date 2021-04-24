package com.etp.questforhealth.persistence;


import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;
import org.springframework.stereotype.Repository;

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
    * Saves the Quest in the persistence.
    *
    * @param quest The quest to be saved.
    * @return The saved quest with an id.
    */
   Quest createQuest(Quest quest);

   void rollbackChanges();
}
