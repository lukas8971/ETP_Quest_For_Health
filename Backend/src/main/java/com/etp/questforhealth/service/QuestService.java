package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;

public interface QuestService {


    /**
     * Returns the quest of the given id.
     *
     * @param id the id of the quest
     * @return a Quest entity with the given id
     * @throws NotFoundException if the id was not found in the persistence or the id is <= 0
     */
    public Quest getOneById(int id) throws NotFoundException;

    /**
     * Saves the Quest in the persistence.
     *
     * @param quest The quest to be saved.
     * @return The saved quest with an id.
     * @throws ValidationException If the quest ist not valid.
     */
    public Quest createQuest(Quest quest) throws ValidationException;
}
