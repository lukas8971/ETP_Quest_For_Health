package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;

import java.util.List;

public interface DoctorDao {

    /**
     * Checks the given email and password are correct and gets the logged in doctor.
     * @param email to login for the doctor.
     * @param password to login for the doctor.
     * @return the logged in Doctor.
     * @throws PersistenceException if something went wrong in the persistent data store.
     * @throws InvalidLoginException if email and/or password are/is wrong
     */
    Doctor checkLogin(String email, String password);

    /**
     * Gets all stored doctors
     * @return a list of all stored doctors
     * @throws PersistenceException if something went wrong in the persistent data store.
     */
    List<Doctor> getAllDoctors();

    /**
     * Gets a doctor with a specific id from the persistent data store.
     * @param id of the doctor to return.
     * @return the doctor with the specific id.
     * @throws PersistenceException if something went wrong in the persistent data store.
     * @throws NotFoundException if no doctor with that id was found.
     */
    Doctor getOneById(int id);

    /**
     * Rollback for changes made during testing.
     */
    void rollbackChanges();
}
