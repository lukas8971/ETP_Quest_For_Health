package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.CharacterLevelDto;
import com.etp.questforhealth.entity.CharacterLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class CharacterLevelMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public CharacterLevel dtoToEntitiy(CharacterLevelDto characterLevelDto){
        LOGGER.trace("dtoToEntity()");
        return new CharacterLevel(characterLevelDto.getId(), characterLevelDto.getTotalStrength(), characterLevelDto.getNeededExp(), characterLevelDto.getLevel(), characterLevelDto.getRank());
    }
    public CharacterLevelDto entityToDto(CharacterLevel characterLevel){
        LOGGER.trace("entityToDto({})", characterLevel.toString());
        return new CharacterLevelDto(characterLevel.getId(), characterLevel.getTotal_strength(), characterLevel.getNeeded_exp(), characterLevel.getLevel(), characterLevel.getRank());
    }
}
