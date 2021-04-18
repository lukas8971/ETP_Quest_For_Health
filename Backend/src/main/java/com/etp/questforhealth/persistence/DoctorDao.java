package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.PersistenceException;

public interface DoctorDao {

    /**
     * Checks the given email and password are correct and gets the logged in doctor.
     * @param email to login for the doctor.
     * @param password to login for the doctor.
     * @return the logged in Doctor.
     * @throws PersistenceException if something is wrong with the persistent data store.
     * @throws InvalidLoginException if email and/or password are/is wrong
     */
    Doctor checkLogin(String email, String password);
}
