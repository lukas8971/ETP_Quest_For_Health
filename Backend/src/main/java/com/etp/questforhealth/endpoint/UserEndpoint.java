package com.etp.questforhealth.endpoint;

import com.etp.questforhealth.endpoint.dto.UserDto;
import com.etp.questforhealth.endpoint.mapper.UserMapper;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(UserEndpoint.BASE_URL)
public class UserEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
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
        LOGGER.info("GET " + BASE_URL);
        List<User> users = userService.getAll();
        List<UserDto> userDtos = new ArrayList<>();
        if (users.size() == 0) return null;
        for (User u: users){
            userDtos.add(userMapper.entityToDto(u));
        }
        return userDtos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody User user){
        LOGGER.info("POST " + BASE_URL);
        try{
            return userMapper.entityToDto(userService.createUser(user));
        } catch(ValidationException e){
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(),e);
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }

    public void rollbackChanges(){
        userService.rollbackChanges();;
    }
}
