package com.etp.questforhealth.endpoint;

import com.etp.questforhealth.endpoint.dto.CharacterLevelDto;
import com.etp.questforhealth.endpoint.mapper.CharacterLevelMapper;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.service.CharacterLevelService;
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
@RequestMapping(CharacterLevelEndpoint.BASE_URL)
public class CharacterLevelEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_URL="/characterLevels";
    private final CharacterLevelService characterLevelService;
    private final CharacterLevelMapper characterLevelMapper;

    @Autowired
    public CharacterLevelEndpoint(CharacterLevelService characterLevelService, CharacterLevelMapper characterLevelMapper) {
        this.characterLevelService = characterLevelService;
        this.characterLevelMapper = characterLevelMapper;
    }

    @GetMapping(value="/{id}")
    public CharacterLevelDto getCharacterLevelById(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/{}",id);
        try{
            return characterLevelMapper.entityToDto(characterLevelService.getCharacterLevelById(id));
        } catch (NotFoundException e){
            LOGGER.error("Could not find character level {] " + e, id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find character level",e);
        }
    }
    @GetMapping(value="/level/{level}")
    public CharacterLevelDto getCharacterLevelByLevel(@PathVariable("level") int level){
        LOGGER.info("GET " + BASE_URL + "/level/{}",level);
        try{
            return characterLevelMapper.entityToDto(characterLevelService.getCharacterLevelByLevel(level));
        } catch (NotFoundException e){
            LOGGER.error("Could not find character level {] " + e, level);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find character level",e);
        }
    }

    @GetMapping(value="/next/{level}")
    public CharacterLevelDto getCharacterNextLevel(@PathVariable("level") int level){
        LOGGER.info("GET " + BASE_URL + "/next/{}",level);
        try{
            return characterLevelMapper.entityToDto(characterLevelService.getCharacterNextLevel(level));
        } catch (NotFoundException e){
            LOGGER.error("Could not find character level {] " + e, level);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find character level",e);
        }
    }
}
