package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.entity.UserEquipment;
import com.etp.questforhealth.exception.NotEnoughGoldException;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ValidationException;

import java.util.List;

public interface EquipmentService {
    List<Equipment> getWornEquipmentFromUserId (int userId);

    /**
     * Gets all the available equipment for a user and type
     * @param user id to get the available equipment
     * @param type of the equipment to display
     * @return a list of all available items
     * @throws ValidationException if the data is incorrect
     * @throws RuntimeException if something went wrong
     */
    List<Equipment> getAvailableEquipmentByTypeAndId(int user, String type);

    /**
     * Buys a new equipment for a user.
     * @param userEquipment object with the equipmentId and userId
     * @return the bought equipment
     * @throws ValidationException if the data is invalid
     * @throws NotEnoughGoldException if the user has not enough gold to buy the equipment
     * @throws RuntimeException if something went wrong
     */
    Equipment buyNewEquipment(UserEquipment userEquipment);

    /**
     * Equips a character with an equipment
     * @param id of the character that should wear the equipment
     * @param equipment that should be worn
     * @return the equipment that is worn
     * @throws ValidationException if the data is invalid
     * @throws RuntimeException if something went wrong
     */
    Equipment equipItem(int id, Equipment equipment);

    /**
     * Unequips a character with an equipment
     * @param userId of the character that should unequip the item
     * @param equipmentId that should be unequipped
     * @return true if unequipped
     * @throws ValidationException if the data is invalid
     * @throws RuntimeException if something went wrong
     */
    boolean unequipItem(int userId, int equipmentId);

    /**
     * Creates a new equipment
     * @param equipment that should be created
     * @return the created equipment
     */
    Equipment createNewEquipment(Equipment equipment);

    /**
     * Gets the equipment of the specific id
     * @param id of the equipment
     * @return the equipment of the id
     */
    Equipment getOneById(int id);
}
