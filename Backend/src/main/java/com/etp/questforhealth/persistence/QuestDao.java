package com.etp.questforhealth.persistence;


import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestDao {

   /**
    *  Returns the quest of the given id.
    * @param id the id of the quest
    * @return a Quest entity with the given id
    * @throws NotFoundException if the id was not found in the persistence
    */
   public Quest getOneById(int id) throws NotFoundException;

}
