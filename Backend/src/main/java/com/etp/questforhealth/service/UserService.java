package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User createUser(User user) throws ServiceException;

    void rollbackChanges();

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

}
