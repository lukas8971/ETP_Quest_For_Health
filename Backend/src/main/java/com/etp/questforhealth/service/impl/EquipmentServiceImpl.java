package com.etp.questforhealth.service.impl;

import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.UserEquipment;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.persistence.EquipmentDao;
import com.etp.questforhealth.persistence.UserDao;
import com.etp.questforhealth.service.EquipmentService;
import com.etp.questforhealth.util.Validator;
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
    private final UserDao userDao;
    private final Validator validator;

    @Autowired
    public EquipmentServiceImpl(EquipmentDao equipmentDao, Validator validator, UserDao userDao) {
        this.equipmentDao = equipmentDao;
        this.validator = validator;
        this.userDao = userDao;
    }

    @Override
    public List<Equipment> getWornEquipmentFromUserId(int userId) {
        LOGGER.trace("getWornEquipmentFromUserId({})", userId);
        try{
            return equipmentDao.getWornEquipmentFromUserId(userId);
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public List<Equipment> getAvailableEquipmentByTypeAndId(int user, String type){
        LOGGER.trace("getAvailableEquipmentByTypeAndId({}, {})", user, type);
        try{
            validator.validateEquipmentByTypeAndId(user, type);
            return equipmentDao.getAvailableEquipmentByTypeAndId(user, type);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Equipment buyNewEquipment(UserEquipment userEquipment){
        LOGGER.trace("buyNewEquipment({})", userEquipment);
        try{
            validator.validateBuyEquipment(userEquipment);
            Equipment ret = equipmentDao.buyNewEquipment(userEquipment);
            if (!userDao.changeUserGold(userEquipment.getUserId(), -1 * ret.getPrice())) throw new ServiceException("Payment error!");
            return ret;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Equipment equipItem(int id, Equipment equipment){
        LOGGER.trace("equipItem({}, {})", id, equipment);
        try{
            validator.validateEquipItem(id, equipment);
            if (!equipmentDao.unequipItem(id, equipment.getType())) throw new ServiceException("Could not unequip currently waring quipment!");
            return equipmentDao.equipItem(id, equipment.getId());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean unequipItem(int userId, int equipmentId) {
        LOGGER.trace("unequipItem({},{})", userId, equipmentId);
        try{
            validator.validateEquipItem(userId, equipmentDao.getOneById(equipmentId));
            return equipmentDao.unequipItem(userId, equipmentId);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
