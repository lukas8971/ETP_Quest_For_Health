package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User createUser(User user) throws ServiceException;

    /**
     * Gets all users that are in treatment at a specific doctor
     * @param doctor where the users are in treatment
     * @return a list of all users in treatment of doctor
     */
    List<User> getAllUsersFromDoctor(int doctor);

    /**
     * Gets the user of the specific id
     * @return the user with the specific id
     * @throws RuntimeException if something went wrong
     * @throws NotFoundException if no user with that id was found.
     */
    User getOneById(int id);

    /**
     * Checks if a User with the specified credentials exist.
     * @param cred user credentials.
     * @return the user with the specific credentials.
     * @throws NotFoundException if no user with that credentials was found.
     */
    User checkLogin(Credentials cred);

    /**
     * Changes the gold of a user
     * @param id of the user to change the gold
     * @param changeValue number of gold that should be changed (can be either positive or negative)
     * @return true if change was successful
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is not valid
     */
    boolean changeUserGold(int id, int changeValue);
}
