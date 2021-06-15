package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.UserEquipment;
import com.etp.questforhealth.entity.enums.EquipmentType;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;

import java.util.List;

public interface EquipmentDao {
    List<Equipment> getWornEquipmentFromUserId (int userId);

    /**
     * Gets the worn equipment of an user by its equipment type
     * @param type of the equipment
     * @param id of the user
     * @return the worn equipment by the user
     * @throws PersistenceException if something went wrong
     */
    Equipment getEquipmentOfTypeWornByUserId(EquipmentType type, int id);

    /**
     * Gets all the available (bought) equipment for a user that is not worn
     * @param type of the equipment
     * @param id of the user
     * @return the equipment that can be worn by an user
     * @throws PersistenceException if something went wrong
     */
    List<Equipment> getAvailableEquipmentToEquip(EquipmentType type, int id);

    /**
     * Gets all the available equipment for a user and type
     * @param user id to get the available equipment
     * @param type of the equipment to display
     * @return a list of all available items
     * @throws PersistenceException if something went wrong in the persistent data store.
     */
    List<Equipment> getAvailableEquipmentByTypeAndId(int user, String type);

    /**
     * Returns the equipment of the given id.
     * @param id of the equipment
     * @return a Equipment item with the given id
     * @throws NotFoundException if the id was not found in the persistence or the id is <= 0
     * @throws PersistenceException if something went wrong in the persistent data store
     */
    Equipment getOneById(int id);

    /**
     * Buys a new equipment for a specific user
     * @param userEquipment object with the userId and equipmentId
     * @return the bought equipment
     * @throws PersistenceException if something went wrong in the persistent data store
     */
    boolean buyNewEquipment(UserEquipment userEquipment);

    /**
     * Gets all the userEquipments that a specific user owns
     * @param id of the user that owns the equipments
     * @return a list of all userEquipments that the user owns
     * @throws PersistenceException if something went wrong
     */
    List<UserEquipment> getAllUserEquipments(int id);

    /**
     * Checks if an user owns a specific item
     * @param usereId id of the user to check
     * @param equipmentId id of the equipment that should be checked
     * @return true if user owns that equipment
     * @throws PersistenceException if something went wrong
     */
    boolean checkIfUserOwnsEquipment(int usereId, int equipmentId);

    /**
     * Gets a worn equipment via a user and its equipment type
     * @param user that wears the equipment type
     * @param type of the equipment
     * @return the equipment or null if no such equipment type is worn
     * @throws PersistenceException if something went wrong
     */
    Equipment getWornEquipmentByTypeAndUser(int user, EquipmentType type);

    /**
     * Unequips an item of the equipment type from an user
     * @param userId of the user that should get unequipped
     * @param type of the equipment that should get unequipped
     * @return true if unequipped (or character did not wear equipment of that type)
     * @throws PersistenceException if something went wrong
     */
    boolean unequipItem(int userId, EquipmentType type);

    /**
     * Unequips an item of the equipment id from an user
     * @param userId of the user that should get unequipped
     * @param equipmentId of the equipment that should get unequipped
     * @return true if unequipped (or character did not wear equipment of that type)
     * @throws PersistenceException if something went wrong
     */
    boolean unequipItem(int userId, int equipmentId);

    /**
     * Equips an item of the equipment type from an user
     * @param userId of the user that should get equipped
     * @param equipmentId of the equipment that should get equipped
     * @return the equipped item
     * @throws PersistenceException if something went wrong
     */
    Equipment equipItem(int userId, int equipmentId);

    /**
     * Creates a new equipment
     * @param equipment that should be created
     * @return the created equipment
     */
    Equipment createNewEquipment(Equipment equipment);
}
