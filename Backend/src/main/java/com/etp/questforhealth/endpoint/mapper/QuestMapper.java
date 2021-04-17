package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.QuestDto;
import com.etp.questforhealth.entity.Quest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class QuestMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public QuestDto entityToDto (Quest quest){
        LOGGER.trace("entityToDto({})",quest);
        if(quest == null) return null;
        return new QuestDto(quest.getId(), quest.getName(), quest.getDescription(), quest.getExp_reward(), quest.getGold_reward(), quest.getRepetition_cycle(), quest.getExp_penalty(), quest.getGold_penalty(), quest.getDoctor());
    }

    public Quest dtoToEntity (QuestDto questDto){
        LOGGER.trace("dtoToEntity({})",questDto);
        if(questDto == null) return null;
        return new Quest(questDto.getId(), questDto.getName(), questDto.getDescription(), questDto.getExp_reward(), questDto.getGold_reward(), questDto.getRepetition_cycle(), questDto.getExp_penalty(), questDto.getGold_penalty(), questDto.getDoctor());
    }

}
