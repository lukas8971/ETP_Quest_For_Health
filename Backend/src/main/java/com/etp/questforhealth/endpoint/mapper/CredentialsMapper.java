package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.CredentialsDto;
import com.etp.questforhealth.entity.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class CredentialsMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Credentials dtoToEntitiy(CredentialsDto credentialsDto){
        LOGGER.trace("dtoToEntity()");
        return new Credentials(credentialsDto.getEmail(), credentialsDto.getPassword());
    }
}
