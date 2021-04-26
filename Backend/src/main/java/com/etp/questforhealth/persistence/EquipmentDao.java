package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.Equipment;

import java.util.List;

public interface EquipmentDao {
    List<Equipment> getWornEquipmentFromUserId (int userId);
}
