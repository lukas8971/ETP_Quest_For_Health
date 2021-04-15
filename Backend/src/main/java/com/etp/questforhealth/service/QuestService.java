package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;

public interface QuestService {


    /**
     * Returns the quest of the given id.
     * @param id the id of the quest
     * @return a Quest entity with the given id
     * @throws NotFoundException if the id was not found in the persistence or the id is <= 0
     */
    public Quest getOneById(int id) throws NotFoundException;
}
