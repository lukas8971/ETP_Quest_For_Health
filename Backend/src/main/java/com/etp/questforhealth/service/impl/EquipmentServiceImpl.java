package com.etp.questforhealth.service.impl;

import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.persistence.EquipmentDao;
import com.etp.questforhealth.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EquipmentDao equipmentDao;

    @Autowired
    public EquipmentServiceImpl(EquipmentDao equipmentDao) {
        this.equipmentDao = equipmentDao;
    }

    @Override
    public List<Equipment> getWornEquipmentFromUserId(int userId) {
        try{
            return equipmentDao.getWornEquipmentFromUserId(userId);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }
}
