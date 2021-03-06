package com.etp.questforhealth.endpoint;

import com.etp.questforhealth.endpoint.dto.PictureDto;
import com.etp.questforhealth.endpoint.dto.StoryChapterDto;
import com.etp.questforhealth.endpoint.mapper.PictureMapper;
import com.etp.questforhealth.endpoint.mapper.StoryChapterMapper;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.service.StoryChapterService;
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
import java.util.List;

@RestController
@RequestMapping(StoryChapterEndpoint.BASE_URL)
public class StoryChapterEndpoint {

    static final String BASE_URL = "/stories";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final StoryChapterService storyChapterService;
    private final StoryChapterMapper storyMapper;
    private final PictureMapper pictureMapper;

    @Autowired
    public StoryChapterEndpoint(StoryChapterService storyChapterService, StoryChapterMapper storyMapper, PictureMapper pictureMapper) {
        this.storyChapterService = storyChapterService;
        this.storyMapper = storyMapper;
        this.pictureMapper = pictureMapper;
    }

    /**
     * Returns a StoryChapterDto of the with the given id.
     * @param id the id of the quest to return
     * @return a QuestDto if the Quest exists
     */
    @GetMapping(value="/{id}")
    public StoryChapterDto getOneById(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/{}",id);
        try{
            return storyMapper.entityToDto(storyChapterService.getOneById(id));
        } catch (NotFoundException e){
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    /**
     * Gets all the previous chapters of a user
     * @param id of the user
     * @return a list of all previous story chapters
     */
    @GetMapping(value = "/allPrev/{id}")
    public List<StoryChapterDto> getAllPreviousChaptersOfUser(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/allPrev/{}",id);
        try {
            User u = new User(id);
            return storyMapper.entityToDto(storyChapterService.getAllPrevChapters(u));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    /**
     * Gets all the next chapters of a user
     * @param id of the user
     * @return a list of all next story chapters
     */
    @GetMapping(value = "/allNext/{id}")
    public List<StoryChapterDto> getAllNextChaptersOfUser(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/allNext/{}",id);
        try {
            User u = new User(id);
            return storyMapper.entityToDto(storyChapterService.getAllNextChapters(u));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    /**
     * Gets the next chapter of a user
     * @param id of the user
     * @return the next story chapters
     */
    @GetMapping(value = "/next/{id}")
    public StoryChapterDto getNextChapterOfUser(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/next/{}",id);
        try {
            User u = new User(id);
            return storyMapper.entityToDto(storyChapterService.getNextChapter(u));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    /**
     * Gets the next chapter information of a user
     * @param id of the user
     * @return the next story chapters
     */
    @GetMapping(value = "/nextInfo/{id}")
    public StoryChapterDto getNextChapterInfo(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/nextInfo/{}",id);
        try {
            User u = new User(id);
            return storyMapper.entityToDto(storyChapterService.getNextChapterInfo(u));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    /**
     * Gets the previous chapter of a user
     * @param id of the user
     * @return the previous story chapters
     */
    @GetMapping(value = "/prev/{id}")
    public StoryChapterDto getPreviousChapterOfUser(@PathVariable("id") int id){
        LOGGER.info("GET " + BASE_URL + "/prev/{}",id);
        try {
            User u = new User(id);
            return storyMapper.entityToDto(storyChapterService.getPrevStoryOfUser(u));
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    /**
     * Gets the picture of the cahapter
     * @param id of the chapter
     * @return the picture base64 encoded
     */
    @GetMapping(value = "/pic/{id}")
    public PictureDto getPicture(@PathVariable("id") int id) {
        LOGGER.info("GET " + BASE_URL + "/pic/{}", id);
        try {
            return pictureMapper.entityToDto(storyChapterService.getPicture(id));
        } catch (NotFoundException e) {
            LOGGER.error("Chapter not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapter not found", e);
        }
    }
}
