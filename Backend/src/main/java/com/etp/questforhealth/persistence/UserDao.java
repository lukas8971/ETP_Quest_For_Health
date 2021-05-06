package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface UserDao {

    List<User> getAll();

    User createUser(User user) throws PersistenceException;

    /**
     * Gets all users from a doctor from the persistent data store.
     * @param doctor that treats patients.
     * @return a list of all users in treatment at doctor.
     * @throws PersistenceException if something went wrong in the persistent data store.
     */
    List<User> getAllUsersFromDoctor(int doctor);

    /**
     * Gets a user with a specific id from the persistent data store.
     * @param id of the user to return.
     * @return the user with the specific id.
     * @throws PersistenceException if something went wrong in the persistent data store.
     * @throws NotFoundException if no doctor with that id was found.
     */
    User getOneById(int id);

    /**
     * Checks if a User with the specified credentials exist.
     * @param cred user credentials.
     * @return the user with the specific credentials.
     * @throws PersistenceException if something went wrong in the persistent data store.
     * @throws NotFoundException if no user with that credentials was found.
     */
    User checkLogin(Credentials cred);

    /**
     * User completed a quest
     * @param userId the id of the user
     * @param questId the id of the quest
     * @param complete true if the user completed the quest, false if the quest's deadline is over
     * @return  true if successful
     */
    boolean completeQuest(int userId, int questId, boolean complete, LocalDate completed_on);

    /**
     * Checks if a User with the specified username exist.
     * @param userName user name.
     * @return true if found, otherwise false;
     * @throws PersistenceException if something went wrong in the persistent data store.
     * @throws NotFoundException if no user with that username was found.
     */
    boolean checkUserNameExists(String userName);

    /**
     * Saves the updated user to the database
     * @param user the user to update
     * @return true if successful
     */
    boolean updateUser(User user);

    /**
     * Adds or subtracts gold and exp to a user
     * @param user the user to change gold and exp
     * @param expChange the change in exp (positive: user gets exp, negative: user loses exp)
     * @param goldChange the change in gold
     * @return the updated user
     */
    User changeUserGoldAndExp (User user, int expChange, int goldChange);


    /**
     * Changes the gold of a user
     * @param id of the user to change the gold
     * @param changeValue number of gold that should be changed (can be either positive or negative)
     * @return true if change was successful
     * @throws PersistenceException if something went wrong
     */
    boolean changeUserGold(int id, int changeValue);
}
