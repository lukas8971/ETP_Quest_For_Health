package com.etp.questforhealth.service;

import com.etp.questforhealth.endpoint.dto.CredentialsDto;
import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;

import java.util.List;

public interface DoctorService {

    /**
     * Checks if the login details are valid
     * @param credentials of the doctor to check the login
     * @return the whole doctor entity if the login is successful
     * @throws RuntimeException if something went wrong.
     * @throws InvalidLoginException if email and/or password are/is wrong.
     */
    Doctor checkLogin(Credentials credentials);

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

    /**
     * Creates a new doctor patient relationship
     * @param doctorId id of the doctor
     * @param userId id of the user to get assigned to the doctor
     * @return true if worked
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is not valid
     */
    boolean assignNewPatient(int doctorId, int userId);

    /**
     * Removes a doctor patient relationship
     * @param doctorId id of the doctor
     * @param userId id of the user to get assigned to the doctor
     * @return true if worked
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is not valid
     */
    boolean removePatient(int doctorId, int userId);
}
