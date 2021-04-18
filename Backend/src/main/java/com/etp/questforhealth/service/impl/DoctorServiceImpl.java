package com.etp.questforhealth.service.impl;

import com.etp.questforhealth.entity.Doctor;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.persistence.DoctorDao;
import com.etp.questforhealth.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class DoctorServiceImpl implements DoctorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DoctorDao doctorDao;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao){this.doctorDao = doctorDao;}

    @Override
    public Doctor checkLogin(String email, String password) {
        LOGGER.trace("checkLogin({}, {})", email, password);
        try {
            return doctorDao.checkLogin(email, password);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
