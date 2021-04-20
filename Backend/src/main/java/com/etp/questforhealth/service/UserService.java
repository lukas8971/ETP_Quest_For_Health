package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;

import java.util.List;

public interface UserService {

    List<User> getAll();

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
}
