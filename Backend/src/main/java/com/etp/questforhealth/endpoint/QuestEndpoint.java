package com.etp.questforhealth.endpoint;


import com.etp.questforhealth.endpoint.dto.QuestDto;
import com.etp.questforhealth.endpoint.mapper.QuestMapper;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.service.QuestService;
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

}
