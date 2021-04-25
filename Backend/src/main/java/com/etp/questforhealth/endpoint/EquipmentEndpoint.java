package com.etp.questforhealth.endpoint;

import com.etp.questforhealth.endpoint.dto.CharacterLevelDto;
import com.etp.questforhealth.endpoint.dto.EquipmentDto;
import com.etp.questforhealth.endpoint.mapper.EquipmentMapper;
import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(EquipmentEndpoint.BASE_URL)
public class EquipmentEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_URL="/equipment";
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @Autowired
    public EquipmentEndpoint(EquipmentService equipmentService, EquipmentMapper equipmentMapper) {
        this.equipmentService = equipmentService;
        this.equipmentMapper = equipmentMapper;
    }

    @GetMapping(value="/wornByUser/{id}")
    public List<EquipmentDto> getEquipmentWornByUserId(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/wornByUser/{}",id);
        List<EquipmentDto> equipmentDtoList = new ArrayList<>();
        try{
            List<Equipment> equipmentList = equipmentService.getWornEquipmentFromUserId(id);
            for(Equipment e: equipmentList){
                equipmentDtoList.add(equipmentMapper.entityToDto(e));
            }
        } catch (ServiceException e){
            LOGGER.error("Something went wrong  " + e, id);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong in the backend",e);
        }
        return equipmentDtoList;
    }
}
