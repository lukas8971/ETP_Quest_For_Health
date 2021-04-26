package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;

import java.util.List;

public interface UserDao {

    List<User> getAll();

    User createUser(User user) throws PersistenceException;

    void rollbackChanges();

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
     * Checks if a User with the specified username exist.
     * @param userName user name.
     * @return true if found, otherwise false;
     * @throws PersistenceException if something went wrong in the persistent data store.
     * @throws NotFoundException if no user with that username was found.
     */
    boolean checkUserNameExists(String userName);
}
