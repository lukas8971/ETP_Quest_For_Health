package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.StoryChapter;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.ValidationException;

import java.util.List;

public interface StoryChapterService {

    /**
     * Gets one story chapter by its id
     * @param id of the story chapter
     * @return the requested story chapter
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is wrong
     */
    StoryChapter getOneById(Integer id);

    /**
     * Gets the next chapter of a chapter
     * @param user that requested
     * @return the next chapter
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is wrong
     */
    StoryChapter getNextChapter(User user);

    /**
     * Gets the next chapter of a chapter
     * @param storyChapter the chapter right now
     * @return the next chapter
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is wrong
     */
    StoryChapter getNextChapter(StoryChapter storyChapter);

    /**
     * Gets the previous chapter of a chapter
     * @param user that requested
     * @return the previous chapter
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is wrong
     */
    StoryChapter getPrevStoryOfUser(User user);

    /**
     * Gets the previous chapter of a chapter
     * @param storyChapter the chapter right now
     * @return the previous chapter
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is wrong
     */
    StoryChapter getPrevChapter(StoryChapter storyChapter);

    /**
     * Gets the next story chapter of a user - level up...
     * @param user that requested
     * @return the next chapter
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is wrong
     */
    StoryChapter getNextStoryOfUser(User user);

    /**
     * Gets all the previous chapters
     * @param user that requested
     * @return all the previous chapters
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is wrong
     */
    List<StoryChapter> getAllPrevChapters(User user);

    /**
     * Gets all the next chapters
     * @param user that requested
     * @return all the next chapters
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is wrong
     */
    List<StoryChapter> getAllNextChapters(User user);
}
