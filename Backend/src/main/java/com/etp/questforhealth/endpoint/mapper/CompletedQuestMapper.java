package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.AcceptedQuestDto;
import com.etp.questforhealth.endpoint.dto.CompletedQuestDto;
import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.entity.CompletedQuest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class CompletedQuestMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public CompletedQuestDto entityToDto(CompletedQuest entity){
        LOGGER.trace("entityToDto({})",entity);
        return new CompletedQuestDto(entity.getQuest() , entity.getUser(), entity.getCompletedOn());
    }

    public List<CompletedQuestDto> entityToDto(List<CompletedQuest> entities) {
        LOGGER.trace("entitiyToDto({})",entities);
        List<CompletedQuestDto> dtos = new ArrayList<CompletedQuestDto>();
        for (CompletedQuest entity:entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }
}
