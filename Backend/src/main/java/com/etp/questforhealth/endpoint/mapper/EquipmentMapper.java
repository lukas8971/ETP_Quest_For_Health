package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.EquipmentDto;
import com.etp.questforhealth.entity.Equipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class EquipmentMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public EquipmentDto entityToDto(Equipment equipment){
        LOGGER.trace("entityToDto({})", equipment.toString());
        return new EquipmentDto(equipment.getId(),equipment.getName(),equipment.getDescription(),equipment.getPrice(),equipment.getStrength(),equipment.getType());
    }

    public Equipment dtoToEntity(EquipmentDto equipmentDto){
        LOGGER.trace("dtoToEntity({})", equipmentDto);
        return new Equipment(equipmentDto.getId(),equipmentDto.getName(),equipmentDto.getDescription(),equipmentDto.getPrice(),equipmentDto.getStrength(),equipmentDto.getType());
    }
}
