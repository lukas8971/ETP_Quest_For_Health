package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.UserDto;
import com.etp.questforhealth.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public UserDto entityToDto (User user){
        if (user == null) return  null;
        return  new UserDto(user.getId(), user.getFirstname(), user.getLastname(), user.getCharacterName(), user.getCharacterStrength(), user.getCharacterLevel(), user.getCharacterExp(), user.getCharacterGold(), user.getPassword(), user.getEmail(), user.getStoryChapter());
    }

    public User dtoToEntity (UserDto userDto){
        if (userDto == null) return null;
        return new User(userDto.getId(), userDto.getFirstname(), userDto.getLastname(), userDto.getCharacterName(), userDto.getCharacterStrength(), userDto.getCharacterLevel(), userDto.getCharacterExp(), userDto.getCharacterGold(), userDto.getPassword(), userDto.getEmail(), userDto.getStoryChapter());
    }

    public List<UserDto> entityToDto (List<User> users){
        LOGGER.trace("entityToDto({})", users);
        if(users == null) return null;
        List<UserDto> usersDto = new ArrayList<>();
        for (User us: users) {
            usersDto.add(new UserDto(us.getId(), us.getFirstname(), us.getLastname()));
        }
        return usersDto;
    }
}
