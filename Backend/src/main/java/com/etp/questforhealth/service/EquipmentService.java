package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.Equipment;

import java.util.List;

public interface EquipmentService {
    List<Equipment> getWornEquipmentFromUserId (int userId);
}
