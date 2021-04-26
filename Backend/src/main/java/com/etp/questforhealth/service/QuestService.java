package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.CreateDoctorQuest;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;

import java.util.List;

public interface QuestService {


    /**
     * Returns the quest of the given id.
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
    public Quest createQuest(CreateDoctorQuest quest) throws ValidationException;

    /**
     * Returns all the available doctor quests for a user.
     * @param user of to assign quests to.
     * @param doctor that assigns the quests to a user.
     * @return a list of all available quests from a doctor to a user.
     * @throws ServiceException if something went wrong.
     * @throws ValidationException if the data is not valid.
     * @throws IllegalArgumentException if the given data is forbidden.
     */
    List<Quest> getAllUserAvailableDoctorQuests(int user, int doctor);

    /**
     * Returns all the assigned doctor quests for a user.
     * @param user of to assign quests to.
     * @param doctor that assigns the quests to a user.
     * @return a list of all assigned quests from a doctor to a user.
     * @throws ServiceException if something went wrong.
     * @throws ValidationException if the data is not valid.
     * @throws IllegalArgumentException if the given data is forbidden.
     */
    List<Quest> getAllUserAssignedDoctorQuests(int user, int doctor);

    /**
     * Deletes an already assigned doctor quest for a patient
     * @param quest the id of the quest that should be unassigned from the user
     * @param user the patient that has the quest assigned to
     * @return true if delete was successful
     * @throws ServiceException if something went wrong.
     */
    boolean deleteAssignedDoctorQuestForUser(int quest, int user);

    /**
     * Adds a new assigned doctor quest for a patient
     * @param acceptedQuest the quest that should be added
     * @return true if successfully added to the user
     * @throws ServiceException if something went wrong.
     * @throws ValidationException if the data is not valid.
     * @throws IllegalArgumentException if the given data is forbidden.
     */
    boolean addAssignedDoctorQuestForUser(AcceptedQuest acceptedQuest);
}
