package com.etp.questforhealth.persistence;

import com.etp.questforhealth.entity.Picture;
import com.etp.questforhealth.entity.StoryChapter;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryChapterDao {
    /**
     * Gets one story chapter by its id
     * @param id of the story chapter
     * @return the requested story chapter
     * @throws PersistenceException if something went wrong
     * @throws NotFoundException if no chapter was found
     */
    StoryChapter getOneById(Integer id);

    /**
     * Gets the next chapter of a chapter
     * @param storyChapter the chapter right now
     * @return the next chapter
     * @throws PersistenceException if something went wrong
     */
    StoryChapter getNextChapter(StoryChapter storyChapter);

    /**
     * Gets the previous chapter of a chapter
     * @param storyChapter the chapter right now
     * @return the previous chapter
     * @throws PersistenceException if something went wrong
     */
    StoryChapter getPrevChapter(StoryChapter storyChapter);

    /**
     * Gets the previous chapter of a user
     * @param user the user that requested
     * @return the previous chapter
     * @throws PersistenceException if something went wrong
     */
    StoryChapter getPrevStoryOfUser(User user);

    /**
     * Gets the next story chapter of a user - level up...
     * @param user that requested
     * @return the next chapter
     * @throws PersistenceException if something went wrong
     */
    StoryChapter getNextStoryOfUser(User user);

    /**
     * Gets all the previous chapters
     * @param user that requested
     * @return all the previous chapters
     * @throws PersistenceException if something went wrong
     */
    List<StoryChapter> getAllPrevChapters(User user);

    /**
     * Gets all the next chapters
     * @param user that requested
     * @return all the next chapters
     * @throws PersistenceException if something went wrong
     */
    List<StoryChapter> getAllNextChapters(User user);

    /**
     * Gets the picture of the chapter
     *
     * @param id the id of the chapter
     * @return the picture
     */
    Picture getPicture(int id);
}
