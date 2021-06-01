package com.etp.questforhealth.endpoint;

import com.etp.questforhealth.endpoint.dto.CredentialsDto;
import com.etp.questforhealth.endpoint.dto.DoctorDto;
import com.etp.questforhealth.endpoint.dto.UserDto;
import com.etp.questforhealth.endpoint.mapper.CredentialsMapper;
import com.etp.questforhealth.endpoint.mapper.UserMapper;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.entity.UserQuest;
import com.etp.questforhealth.entity.UserQuests;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.NotFoundException;
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
    private final CredentialsMapper credentialsMapper;


    @Autowired
    public UserEndpoint(UserService userService, UserMapper userMapper, CredentialsMapper credentialsMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.credentialsMapper = credentialsMapper;
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

    /**
     * Gets all the patients that are in treatment at a doctor
     * @param doctor the doctor that has these patients in treatment
     * @return a list of all patients of a doctor
     */
    @GetMapping(value="/doctor/{id}")
    public List<UserDto> getAllUsersFromDoctor(@PathVariable("id") int doctor){
        LOGGER.info("GET " + BASE_URL + "/doctor/{}", doctor);
        return userMapper.entityToDto(userService.getAllUsersFromDoctor(doctor));
    }

    /**
     * Get all users that are not in treatment at a doctor
     * @param doctor that does not have these patients
     * @return list of users that are not in treatment
     */
    @GetMapping(value="/doctorNot/{id}")
    public List<UserDto> getAllNotUsersFromDoctor(@PathVariable("id") int doctor){
        LOGGER.info("GET " + BASE_URL + "/doctorNot/{}", doctor);
        return userMapper.entityToDto(userService.getAllNotUsersFromDoctor(doctor));
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

    /**
     * Returns the strength of a user
     * @param id of the user to get its strength
     * @return the strength of a user
     */
    @GetMapping(value="/{id}/strength")
    public int getUserStrength(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/{}/strength",id);
        try{
            return userService.getUserStrength(id);
        } catch (NotFoundException e){
            LOGGER.error("Could not find user with id {} " + e, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user",e);
        }
    }

    @PostMapping(value="/completeQuest")
    @ResponseStatus(HttpStatus.OK)
    public UserDto completeQuest(@RequestBody UserQuest userQuest){
        LOGGER.info("POST" + BASE_URL + "/completeQuest");
        try{
            return userMapper.entityToDto(userService.completeQuest(userQuest.getUser(), userQuest.getQuest()));
        } catch (ServiceException e){
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }
    @PostMapping(value="/dismissQuests")
    @ResponseStatus(HttpStatus.OK)
    public UserDto dismissQuest(@RequestBody UserQuests userQuests){
        LOGGER.info("POST" + BASE_URL + "/dismissQuests");
        try{
            return userMapper.entityToDto(userService.dismissMissedQuests(userQuests.getUser(), userQuests.getQuests()));
        } catch (ServiceException e){
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody User user){
        LOGGER.info("POST " + BASE_URL + " " + user);
        try{
            return userMapper.entityToDto(userService.createUser(user));
        } catch(ValidationException e){
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(),e);
        } catch (ServiceException e){
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }

    @PostMapping(value="/login")
    public UserDto checkLogin(@RequestBody CredentialsDto credentialsDto){
        LOGGER.info("POST " + BASE_URL + "/login");
        try{
            return userMapper.entityToDto(userService.checkLogin(credentialsMapper.dtoToEntitiy(credentialsDto)));
        } catch (InvalidLoginException e){
            LOGGER.error("Wrong username or password!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    /**
     * Checks if a user is possible to get to the next chapter
     * Also updates the chapter if a user is possible to do so
     * @param id of the user
     * @return true if possible AND updated
     */
    @GetMapping(value = "/checkStory/{id}")
    public boolean checkUserForNextStoryAndUpdate(@PathVariable("id") int id) {
        LOGGER.info("GET " + BASE_URL + "/checkStory/{}",id);
        User u = new User(id);
        return userService.checkUserForNextStoryAndUpdate(u);
    }

    /**
     * Gets a leaderboard for the user
     * @param id of the user
     * @return a list of all users in the leaderboard - sorted by strength and exp
     */
    @GetMapping(value = "/leaderboard/{id}")
    public List<UserDto> getLeaderboardForUser(@PathVariable("id") int id) {
        LOGGER.info("GET " + BASE_URL + "/leaderboard/{}",id);
        return userMapper.leaderboardEntityToDto(userService.getLeaderboardForUser(new User(id)));
    }
}
