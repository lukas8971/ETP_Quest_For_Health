package com.etp.questforhealth.endpoint;

import com.etp.questforhealth.endpoint.dto.UserDto;
import com.etp.questforhealth.endpoint.mapper.UserMapper;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(value="/doctor/{id}")
    public List<UserDto> getAllUsersFromDoctor(@PathVariable("id") int doctor){
        LOGGER.info("GET " + BASE_URL + "/doctor/{}", doctor);
        return userMapper.entityToDto(userService.getAllUsersFromDoctor(doctor));
    }

    /**
     * Returns a UserDto with the given id.
     * @param id of the user to return
     * @return a DoctorDto
     */
    @GetMapping(value="/{id}")
    public UserDto getOneById(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/{}",id);
        try{
            return userMapper.entityToDto(userService.getOneById(id));
        } catch (NotFoundException e){
            LOGGER.error("Could not find user with id {} " + e, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user",e);
        }
    }
}
