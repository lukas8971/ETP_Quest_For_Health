package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.exception.InvalidLoginException;

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
}
