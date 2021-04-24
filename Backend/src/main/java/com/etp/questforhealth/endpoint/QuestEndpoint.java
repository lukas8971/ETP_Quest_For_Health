package com.etp.questforhealth.endpoint;


import com.etp.questforhealth.endpoint.dto.QuestDto;
import com.etp.questforhealth.endpoint.mapper.QuestMapper;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.service.QuestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(QuestEndpoint.BASE_URL)
public class QuestEndpoint {
    static final String BASE_URL = "/quests";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final QuestService questService;
    private final QuestMapper questMapper;

    @Autowired
    public QuestEndpoint(QuestService questService, QuestMapper questMapper){
        this.questService = questService;
        this.questMapper = questMapper;
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
     * @param questDto The quest to be saved.
     * @return The quest with a new id.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestDto createQuest(@RequestBody QuestDto questDto){
        LOGGER.info("POST "+BASE_URL, questDto);
        try{
            return questMapper.entityToDto(questService.createQuest(questMapper.dtoToEntity(questDto)));
        } catch(ValidationException e1){
            LOGGER.error("Validation exception while creating new Quest {} " + e1 + "\n",questDto);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e1.getMessage(), e1);
        }
    }

}
