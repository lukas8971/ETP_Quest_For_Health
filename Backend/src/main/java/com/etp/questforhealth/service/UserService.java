package com.etp.questforhealth.service;

import com.etp.questforhealth.entity.Credentials;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.NotFoundException;
import com.etp.questforhealth.exception.PersistenceException;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User createUser(User user) throws ServiceException;

    /**
     * Gets all users that are in treatment at a specific doctor
     * @param doctor where the users are in treatment
     * @return a list of all users in treatment of doctor
     */
    List<User> getAllUsersFromDoctor(int doctor);

    /**
     * Gets all users that are not in treatment at a specific doctor
     * @param doctor where the users are not in treatment
     * @return a list of all users not in treatment of doctor
     */
    List<User> getAllNotUsersFromDoctor(int doctor);

    /**
     * Gets the user of the specific id
     * @return the user with the specific id
     * @throws RuntimeException if something went wrong
     * @throws NotFoundException if no user with that id was found.
     */
    User getOneById(int id);

    /**
     * Returns the strength of a user
     * @param id of the user to get its strength
     * @return the strength of a user
     */
    int getUserStrength(int id);

    /**
     * Checks if a User with the specified credentials exist.
     * @param cred user credentials.
     * @return the user with the specific credentials.
     * @throws NotFoundException if no user with that credentials was found.
     */
    User checkLogin(Credentials cred);

    /**
     * User completed a quest. For that, his exp., gold and level will be updated
     * @param user the user who completed the quest
     * @param quest the quest
     * @return the updated user
     */
    User completeQuest(User user, Quest quest);

    /**
     * Dismisses missed Quests so that they do not appear again and sets gold, exp and character level according to the penalties
     * @param user the user who missed the quests
     * @param missedQuests the missed quests
     * @return the updated user
     */
    User dismissMissedQuests(User user, List<Quest> missedQuests);

    /**
     * Changes the gold of a user
     * @param id of the user to change the gold
     * @param changeValue number of gold that should be changed (can be either positive or negative)
     * @return true if change was successful
     * @throws RuntimeException if something went wrong
     * @throws ValidationException if the data is not valid
     */
    boolean changeUserGold(int id, int changeValue);

    /**
     * Sets the user to the next story chapter
     * @param user that gets to the next story
     * @return true if worked
     */
    boolean setNextStoryChapter(User user);

    /**
     * Checks if a user has enough strength to get to the next chapter
     * and updates if possible
     * @param user of which the strength changed
     * @return true if next chapter is reached
     */
    boolean checkUserForNextStoryAndUpdate(User user);

    /**
     * Gets the leaderboard entries for a specific user
     * @param user that wants to view the leaderboard
     * @return a list of all users
     */
    List<User> getLeaderboardForUser(User user);
}
