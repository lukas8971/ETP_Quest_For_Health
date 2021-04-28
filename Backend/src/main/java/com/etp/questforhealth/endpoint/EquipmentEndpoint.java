package com.etp.questforhealth.endpoint;

import com.etp.questforhealth.endpoint.dto.EquipmentDto;
import com.etp.questforhealth.endpoint.mapper.EquipmentMapper;
import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.UserEquipment;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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

    /**
     * Gets all the available equipment for a user and type
     * @param user id to get the available equipment
     * @param type of the equipment to display
     * @return a list of all available items
     */
    @GetMapping(value="/shop")
    @ResponseBody
    public List<EquipmentDto> getAvailableEquipmentByTypeAndId(@RequestParam int user, @RequestParam String type){
        LOGGER.info("GET " + BASE_URL + "/shop?user={}&type={}", user, type);
        List<EquipmentDto> equipmentDtoList = new ArrayList<>();
        try {
            List<Equipment> equipmentList = equipmentService.getAvailableEquipmentByTypeAndId(user, type);
            for(Equipment e: equipmentList){
                equipmentDtoList.add(equipmentMapper.entityToDto(e));
            }
            return equipmentDtoList;
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    /**
     * Buys a new equipment for a user
     * @param userEquipment with the equipment of the id and the id of the user
     * @return the bought equipment
     */
    @PostMapping(value = "/buy")
    @ResponseStatus(HttpStatus.CREATED)
    public EquipmentDto buyNewEquipment(@RequestBody UserEquipment userEquipment) {
        LOGGER.info("POST " + BASE_URL + "/buy" + userEquipment);
        try {
            return equipmentMapper.entityToDto(equipmentService.buyNewEquipment(userEquipment));
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    /**
     * Equips an item for a user
     * @param id of the user that should wear the new equipment
     * @param equipmentDto that should be worn
     * @return the newly equipped item
     */
    @PostMapping(value = "/equip/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EquipmentDto equipItem(@PathVariable("id") int id, @RequestBody EquipmentDto equipmentDto) {
        LOGGER.info("POST " + BASE_URL + "/equip/{}" + equipmentDto, id);
        try {
            return equipmentMapper.entityToDto(equipmentService.equipItem(id, equipmentMapper.dtoToEntity(equipmentDto)));
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }
}
