package com.etp.questforhealth.endpoint;

import com.etp.questforhealth.endpoint.dto.UserDto;
import com.etp.questforhealth.endpoint.mapper.UserMapper;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(UserEndpoint.BASE_URL)
public class UserEndpoint {
    static final String BASE_URL="/users";
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserEndpoint(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserDto> getAll(){
        List<User> users = userService.getAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User u: users){
            userDtos.add(userMapper.entityToDto(u));
        }
        return userDtos;
    }
}
