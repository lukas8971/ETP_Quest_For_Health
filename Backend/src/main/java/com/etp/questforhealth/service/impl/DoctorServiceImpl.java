package com.etp.questforhealth.service.impl;

import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.DoctorDao;
import com.etp.questforhealth.service.DoctorService;
import com.etp.questforhealth.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DoctorDao doctorDao;
    private final Validator validator;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao, Validator validator){
        this.doctorDao = doctorDao;
        this.validator = validator;
    }

    @Override
    public Doctor checkLogin(Credentials credentials) {
        LOGGER.trace("checkLogin({})", credentials.getEmail());
        try {
            return doctorDao.checkLogin(credentials);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Doctor> getAllDoctors() {
        LOGGER.trace("getAllDoctors()");
        return doctorDao.getAllDoctors();
    }

    @Override
    public Doctor getOneById(int id){
        LOGGER.trace("getOneById({})", id);
        if (id < 0) {
            throw new ValidationException("ID has to be greater than 0");
        }
        try {
            return doctorDao.getOneById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean assignNewPatient(int doctorId, int userId) {
        LOGGER.trace("assignNewPatient({}, {})", doctorId, userId);
        validator.validateNewUserDoctorRelationship(doctorId, userId);
        try {
            return doctorDao.assignNewPatient(doctorId, userId);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean removePatient(int doctorId, int userId) {
        LOGGER.trace("removePatient({}, {})", doctorId, userId);
        try {
            if (doctorId < 0 || userId < 0) throw new ValidationException("Id must be greater than 0");
            return doctorDao.removePatient(doctorId, userId);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
