package com.etp.questforhealth.persistence;

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
}
