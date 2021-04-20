package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.NotFoundException;

import java.util.List;

public interface DoctorService {

    /**
     * Checks if the login details are valid
     * @param email of the doctor to check the login
     * @param password of the doctor to check the login
     * @return the whole doctor entity if the login is successful
     * @throws RuntimeException if something went wrong.
     * @throws InvalidLoginException if email and/or password are/is wrong.
     */
    Doctor checkLogin(String email, String password);

    /**
     * Gets all stored doctors
     * @return a list of all stored doctors
     * @throws RuntimeException if something went wrong.
     */
    List<Doctor> getAllDoctors();

    /**
     * Gets the doctor of the specific id
     * @return the doctor with the specific id
     * @throws RuntimeException if something went wrong
     * @throws NotFoundException if no doctor with that id was found.
     */
    Doctor getOneById(int id);
}
