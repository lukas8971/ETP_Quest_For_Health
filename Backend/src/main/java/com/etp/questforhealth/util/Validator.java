package com.etp.questforhealth.util;

import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void validateNewUser (User user){
        LOGGER.trace("validateNewUser({})", user.toString());
        String errorMsg = "";
        if(user.getFirstname() == null || user.getFirstname().isBlank()){
            errorMsg += "You must enter a firstname. \n";
        }
        if(user.getLastname() == null || user.getLastname().isBlank()){
            errorMsg += "You must enter a lastname. \n";
        }
        if(user.getCharacterName() == null || user.getCharacterName().isBlank()){
            errorMsg += "You must enter a character-name. \n";
        }
        if(user.getPassword() == null || user.getPassword().isBlank()){
            errorMsg += "You must enter a password. \n";
        }
        if(!errorMsg.isBlank()){
            throw new ValidationException(errorMsg);
        }
    }
}
