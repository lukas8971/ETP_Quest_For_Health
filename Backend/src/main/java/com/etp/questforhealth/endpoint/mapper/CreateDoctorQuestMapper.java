package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.CreateDoctorQuestDto;
import com.etp.questforhealth.entity.CreateDoctorQuest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class CreateDoctorQuestMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final QuestMapper questMapper;
    private final CredentialsMapper credentialsMapper;

    public CreateDoctorQuestMapper(QuestMapper questMapper, CredentialsMapper credentialsMapper) {
        this.questMapper = questMapper;
        this.credentialsMapper = credentialsMapper;
    }


    public CreateDoctorQuest dtoToEntity(CreateDoctorQuestDto dto){
        LOGGER.trace("dtoToEntity({})",dto);
        return new CreateDoctorQuest(credentialsMapper.dtoToEntitiy(dto.getCredentials()), questMapper.dtoToEntity(dto.getQuest()));
    }

}
