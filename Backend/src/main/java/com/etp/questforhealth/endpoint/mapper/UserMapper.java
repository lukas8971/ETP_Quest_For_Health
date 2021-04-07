package com.etp.questforhealth.endpoint.mapper;

import com.etp.questforhealth.endpoint.dto.UserDto;
import com.etp.questforhealth.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto entityToDto (User user){
        if (user == null) return  null;
        return  new UserDto(user.getId(), user.getFirstname(), user.getLastname());
    }

    public User dtoToEntity (UserDto userDto){
        if (userDto == null) return null;
        return new User(userDto.getId(), userDto.getFirstname(), userDto.getLastname());
    }
}
