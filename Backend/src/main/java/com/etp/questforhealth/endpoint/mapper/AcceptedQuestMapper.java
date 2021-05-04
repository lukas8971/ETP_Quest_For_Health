package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.AcceptedQuestDto;
import com.etp.questforhealth.entity.AcceptedQuest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class AcceptedQuestMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public AcceptedQuestDto entityToDto(AcceptedQuest entity){
        LOGGER.trace("entityToDto({})",entity);
        return new AcceptedQuestDto(entity.getQuest() , entity.getUser(), entity.getAcceptedOn());
    }

    public List<AcceptedQuestDto> entityToDto(List<AcceptedQuest> entities) {
        LOGGER.trace("entitiyToDto({})",entities);
        List<AcceptedQuestDto> dtos = new ArrayList<AcceptedQuestDto>();
        for (AcceptedQuest entity:entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }
}
