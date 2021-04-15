package com.etp.questforhealth.persistence;


import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestDao {

   public Quest getOneById(int id) throws NotFoundException;

}
