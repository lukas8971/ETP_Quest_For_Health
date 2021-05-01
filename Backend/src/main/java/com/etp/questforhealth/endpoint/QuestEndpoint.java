package com.etp.questforhealth.endpoint;


import com.etp.questforhealth.endpoint.dto.CreateDoctorQuestDto;
import com.etp.questforhealth.endpoint.dto.QuestDto;
import com.etp.questforhealth.endpoint.mapper.CreateDoctorQuestMapper;
import com.etp.questforhealth.endpoint.mapper.QuestMapper;
import com.etp.questforhealth.entity.AcceptedQuest;
import com.etp.questforhealth.exception.InvalidLoginException;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.service.QuestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;


@RestController
@RequestMapping(QuestEndpoint.BASE_URL)
public class QuestEndpoint {
    static final String BASE_URL = "/quests";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final QuestService questService;
    private final QuestMapper questMapper;
    private final CreateDoctorQuestMapper createDoctorQuestMapper;

    @Autowired
    public QuestEndpoint(QuestService questService, QuestMapper questMapper, CreateDoctorQuestMapper createDoctorQuestMapper){
        this.questService = questService;
        this.questMapper = questMapper;
        this.createDoctorQuestMapper = createDoctorQuestMapper;
    }

    /**
     * Returns a QuestDto of the with the given id.
     * The id must be greater than 0.
     * The repetition_cycle is in ISO-8601 format.
     * @param id the id of the quest to return
     * @return a QuestDto if the Quest exists
     */
    @GetMapping(value="/{id}")
    public QuestDto getOneById(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/{}",id);
        try{
            return questMapper.entityToDto(questService.getOneById(id));
        } catch (NotFoundException e1){
            LOGGER.error("Could not find quest with id {} " + e1 + "\n", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest not found",e1);
        }
    }

    /**
     * Saves the given Quest.
     * @param createDoctorQuestDto The quest to be saved.
     * @return The quest with a new id.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestDto createQuest(@RequestBody CreateDoctorQuestDto createDoctorQuestDto){
        LOGGER.info("POST "+BASE_URL, createDoctorQuestDto);
        try{
            return questMapper.entityToDto(questService.createQuest(createDoctorQuestMapper.dtoToEntity(createDoctorQuestDto)));
        } catch(ValidationException e1){
            LOGGER.error("Validation exception while creating new Quest {} " + e1 + "\n",createDoctorQuestDto);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e1.getMessage(), e1);
        } catch(InvalidLoginException e2){
            LOGGER.error("InvalidLoginException while creating new Quest {} " + e2 + "\n", createDoctorQuestDto);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password!", e2);
        }
    }


    /**
     * Gets all the quests available for a doctor to a user
     * @param user to assigned quests to
     * @param doctor who is assigning quests to a user
     * @return a list of all available doctor quests for a user
     */
    @GetMapping(value="/available")
    @ResponseBody
    public List<QuestDto> getAllUserAvailableDoctorQuests(@RequestParam int user, @RequestParam int doctor){
        LOGGER.info("GET " + BASE_URL + "/available?user={}&doctor={}", user, doctor);
        try{
            return questMapper.entityToDto(questService.getAllUserAvailableDoctorQuests(user, doctor));
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    /**
     * Accepts a quest for a user
     * @param user the user who accepts the quest
     * @param quest the quest to accept
     */
    @GetMapping(value="/accept")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean acceptQuest (@RequestParam int user, @RequestParam int quest){
        LOGGER.info("PUT " + BASE_URL + "accept/?user={}&quest={}", user,quest);
        try{
             return questService.acceptQuest(user,quest);
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not accept quest", e);
        }
    }

    /**
     * Gets all new normal quests for a user
     * @param user to get new quests for
     * @return a list of all new normal quests for the user
     */
    @GetMapping(value="/newQuests")
    @ResponseBody
    public List<QuestDto> getNewQuestsForUserId(@RequestParam int user){
        LOGGER.info("GET " + BASE_URL + "/newQuests?user={}", user);
        return questMapper.entityToDto(questService.getNewQuestsForUserId(user));
    }

    /**
     * Gets all repetitive due quests for User
     * @param id to get due quests for user
     * @return a list of all repetitive quests for the user
     */
    @GetMapping(value="/dueQuests/{id}")
    @ResponseBody
    public List<QuestDto> getAllQuestsDueForUser(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/dueQuests/{}", id);
        return questMapper.entityToDto(questService.getAllQuestsDueForUser(id));
    }

    /**
     * Gets all missed repetitive quests for User
     * @param id to get due quests for user
     * @return a list of all missed quests for the user
     */
    @GetMapping(value="/missedQuests/{id}")
    @ResponseBody
    public List<QuestDto> getAllMissedQuestsForUser(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/missedQuests/{}", id);
        return questMapper.entityToDto(questService.getAllMissedQuestsForUser(id));
    }

    /**
     * Gets all one-time open quests for User
     * @param id to get open quests for user
     * @return a list of all open one-time quests for the user
     */
    @GetMapping(value="/openOneTimeQuests/{id}")
    @ResponseBody
    public List<QuestDto> getAllOpenOneTimeQuestsForUser(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/");
        return questMapper.entityToDto(questService.getAllOpenOneTimeQuestsForUser(id));
    }

    /**
     * Gets all the quests assigned from a doctor to a user
     * @param user to assigned quests to
     * @param doctor who is assigning quests to a user
     * @return a list of all assigned doctor quests for a user
     */
    @GetMapping(value="/assigned")
    @ResponseBody
    public List<QuestDto> getAllUserAssignedDoctorQuests(@RequestParam int user, @RequestParam int doctor){
        LOGGER.info("GET " + BASE_URL + "/assigned?user={}&doctor={}", user, doctor);
        try {
            return questMapper.entityToDto(questService.getAllUserAssignedDoctorQuests(user, doctor));
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    /**
     * Delete an already assigned quest from a user
     * @param quest the quest that should be unnassigned
     * @param user patient the quest is assigned to
     * @return true if delete was successful.
     */
    @DeleteMapping(value = "/assigned/{quest}/{user}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteAssignedDoctorQuestForUser(@PathVariable("quest") int quest, @PathVariable("user") int user) {
        LOGGER.info("DELETE " + BASE_URL + "/assigned/{}/{}", quest, user);
        return questService.deleteAssignedDoctorQuestForUser(quest, user);
    }

    /**
     * Adds a new assigned quest from a user
     * @param acceptedQuest the quest that should be added
     * @return true if successfully assigned to user
     */
    @PostMapping(value = "/assigned")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addAssignedDoctorQuestForUser(@RequestBody AcceptedQuest acceptedQuest) {
        LOGGER.info("POST " + BASE_URL + "/assigned");
        try {
            return questService.addAssignedDoctorQuestForUser(acceptedQuest);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }
}
