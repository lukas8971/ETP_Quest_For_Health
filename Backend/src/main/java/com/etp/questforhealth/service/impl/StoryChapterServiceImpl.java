package com.etp.questforhealth.service.impl;

import com.etp.questforhealth.entity.Picture;
import com.etp.questforhealth.entity.StoryChapter;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.StoryChapterDao;
import com.etp.questforhealth.service.StoryChapterService;
import com.etp.questforhealth.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class StoryChapterServiceImpl implements StoryChapterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Validator validator;
    private final StoryChapterDao storyChapterDao;

    @Autowired
    public StoryChapterServiceImpl(Validator validator, StoryChapterDao storyChapterDao) {
        this.validator = validator;
        this.storyChapterDao = storyChapterDao;
    }

    @Override
    public StoryChapter getOneById(Integer id) {
        LOGGER.trace("getOneById({})", id);
        if (id == null || id < 0) throw new ValidationException("Id must be greater than 0!");
        try {
            return storyChapterDao.getOneById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public StoryChapter getNextChapter(User user) {
        LOGGER.trace("getNextChapter({})", user);
        try {
            validator.validateNextStoryChapter(user);
            return storyChapterDao.getNextChapter(storyChapterDao.getOneById(user.getStoryChapter()));
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public StoryChapter getNextChapter(StoryChapter storyChapter) {
        LOGGER.trace("getNextChapter({})", storyChapter);
        try {
            validator.validateNextStoryChapter(storyChapter);
            return storyChapterDao.getNextChapter(storyChapter);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public StoryChapter getPrevStoryOfUser(User user) {
        LOGGER.trace("getPrevStoryOfUser({})", user);
        try {
            validator.validatePrevStoryChapter(user);
            return storyChapterDao.getPrevStoryOfUser(user);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public StoryChapter getPrevChapter(StoryChapter storyChapter) {
        LOGGER.trace("getPrevChapter({})", storyChapter);
        try {
            validator.validatePrevStoryChapter(storyChapter);
            return storyChapterDao.getPrevChapter(storyChapter);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public StoryChapter getNextStoryOfUser(User user) {
        LOGGER.trace("getNextStoryOfUser({})", user);
        try {
            validator.validateNextStoryChapter(user);
            return storyChapterDao.getNextStoryOfUser(user);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<StoryChapter> getAllPrevChapters(User user) {
        LOGGER.trace("getAllPrevChapters({})", user);
        try {
            return storyChapterDao.getAllPrevChapters(user);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<StoryChapter> getAllNextChapters(User user) {
        LOGGER.trace("getAllNextChapters({})", user);
        try {
            return storyChapterDao.getAllNextChapters(user);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Picture getPicture(int id) {
        LOGGER.trace("getPicture({})",id);
        //check if chapter exists
        getOneById(id);
        return storyChapterDao.getPicture(id);
    }
}
