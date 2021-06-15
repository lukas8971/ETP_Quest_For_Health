package com.etp.questforhealth.unit.service;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.ServiceException;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.UserDao;
import com.etp.questforhealth.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class UserServiceTest {

    @BeforeEach
    public void testData(){
        DatabaseTestData.insertTestData();
    }

    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;

    @Test
    @DisplayName("Requesting an initial user list should return not null")
    public void requestingEmptyUserList_shouldReturnNull() {
        assertNotNull(userService.getAll());
    }

    @Test
    @DisplayName("Creating a valid user should work")
    public void createUser_shouldWork(){
        User u = userService.createUser(TestData.getNewWorkingUser());
        assert(u.getId()!=0);
    }

    @Test
    @DisplayName("Creating an invalid user should not work")
    public void createUser_shouldThrowValidationException(){
        assertThrows(ValidationException.class, () -> userService.createUser(TestData.getNewUser()));
    }

    @Test
    @DisplayName("Changing the gold amount of a user to still be positive should not throw any exception")
    public void changingUserGold_shouldNotThrow(){
        assertDoesNotThrow(() -> {
            User u = userService.createUser(TestData.getNewWorkingUserDifferentCharacter());
            assertNotNull(u);
            assertTrue(userService.changeUserGold(u.getId(), 500000));
            assertEquals(userService.getOneById(u.getId()).getCharacterGold(), 500000);
        });
    }

    @Test
    @DisplayName("Completing a valid Quest with a valid user should work")
    public void completeQuest_shouldWork(){
        User user = userService.getOneById(1);
        int gold = user.getCharacterGold();
        int exp = user.getCharacterExp();
        User updatedUser = userService.completeQuest(user, TestData.getNewStandardQuest(1)); // Id is one because that one already exists
        assertAll(
                ()-> assertTrue(gold < updatedUser.getCharacterGold()),
                ()-> assertTrue(exp < updatedUser.getCharacterExp())
        );
    }

    @Test
    @DisplayName("Completing a valid Quest as an invalid user should throw a ServiceException")
    public void completeQuest_invalidUser_shouldThrowServiceException(){
        User user = TestData.getNewWorkingUser();
        user.setId(-1);
        assertThrows(IllegalArgumentException.class, () -> userService.completeQuest(user, TestData.getNewStandardQuest(1)));
    }

    @Test
    @DisplayName ("Completing an invalid Quest should throw a ServiceException")
    public void completeQuest_invalidQuest_shouldThrowServiceException(){
        User user = userService.getOneById(1);
        assertThrows(IllegalArgumentException.class, () -> userService.completeQuest(user, TestData.getNewStandardQuest(-1)));
    }

    @Test
    @DisplayName ("Completing a Quest with max. exp should lead to a level-up")
    public void completeQuest_levelUp_shouldWork(){
        User user = userService.getOneById(1);
        int characterLevel = user.getCharacterLevel();
        Quest quest = TestData.getNewStandardQuest(1);
        quest.setExp_reward(Integer.MAX_VALUE-user.getCharacterExp()); //to prevent overflow
        User updatedUser = userService.completeQuest(user,quest);
        assertNotEquals(characterLevel, updatedUser.getCharacterGold());
    }

    @Test
    @DisplayName("Dismissing missed Quests with valid user and valid Quests should work")
    public void dismissMissedQuests_shouldWork(){
        User user = userService.getOneById(1);
        int gold = user.getCharacterGold();
        int exp = user.getCharacterExp();
        List<Quest> missedQuests = new ArrayList<>();
        Quest quest1 = TestData.getNewStandardQuest(1);
        quest1.setExp_penalty(10);
        quest1.setGold_penalty(5);
        Quest quest2 = TestData.getNewStandardQuest(2);
        quest2.setExp_penalty(20);
        quest2.setGold_penalty(10);
        missedQuests.add(quest1);
        missedQuests.add(quest2);
        User updatedUser = userService.dismissMissedQuests(user, missedQuests);
        assertAll(
                () -> assertTrue(gold > updatedUser.getCharacterGold()),
                () -> assertTrue(exp > updatedUser.getCharacterExp())
        );
    }
    @Test
    @DisplayName("Dismissing missed Quests with a higher exp and gold penalty should set user's gold and exp to 0")
    public void dismissMissedQuests_noExpOrGoldDebt_shouldWork(){
        User user = userService.getOneById(1);
        List<Quest> missedQuests = new ArrayList<>();
        Quest quest1 = TestData.getNewStandardQuest(1);
        quest1.setExp_penalty(Integer.MAX_VALUE);
        quest1.setGold_penalty(Integer.MAX_VALUE);
        Quest quest2 = TestData.getNewStandardQuest(2);
        quest2.setExp_penalty(Integer.MAX_VALUE);
        quest2.setGold_penalty(Integer.MAX_VALUE);
        missedQuests.add(quest1);
        missedQuests.add(quest2);
        User updatedUser = userService.dismissMissedQuests(user, missedQuests);
        assertAll(
                () -> assertEquals(0,updatedUser.getCharacterGold()),
                () -> assertEquals(0,updatedUser.getCharacterExp())
        );
    }

    @Test
    @DisplayName("Dismissing missed Quests as an invalid user should throw a ServiceException")
    public void dismissMissedQuests_invalidUser_shouldThrowServiceException(){
        User user = userService.getOneById(1);
        user.setId(-1);
        List<Quest> missedQuests = new ArrayList<>();
        Quest quest1 = TestData.getNewStandardQuest(1);
        Quest quest2 = TestData.getNewStandardQuest(2);
        missedQuests.add(quest1);
        missedQuests.add(quest2);
        assertThrows(IllegalArgumentException.class, () -> userService.dismissMissedQuests(user, missedQuests));
    }

    @Test
    @DisplayName("Dismissing invalid Quests  should throw a ServiceException")
    public void dismissMissedQuests_invalidQuests_shouldThrowServiceException(){
        User user = userService.getOneById(1);
        user.setId(-1);
        List<Quest> missedQuests = new ArrayList<>();
        Quest quest1 = TestData.getNewStandardQuest(-1);
        Quest quest2 = TestData.getNewStandardQuest(-2);
        missedQuests.add(quest1);
        missedQuests.add(quest2);
        assertThrows(IllegalArgumentException.class, () -> userService.dismissMissedQuests(user, missedQuests));
    }

    @Test
    @DisplayName("Dismissing Quests with a high enough exp penalty should result in a level-down")
    public void dismissMissedQuests_levelDown_shouldWork(){
        User user = userService.getOneById(1);
        int newLevel= 2;
        user.setCharacterLevel(newLevel);
        List<Quest> missedQuests = new ArrayList<>();
        Quest quest1 = TestData.getNewStandardQuest(1);
        quest1.setExp_penalty(Integer.MAX_VALUE);
        quest1.setGold_penalty(Integer.MAX_VALUE);
        Quest quest2 = TestData.getNewStandardQuest(2);
        quest2.setExp_penalty(Integer.MAX_VALUE);
        quest2.setGold_penalty(Integer.MAX_VALUE);
        missedQuests.add(quest1);
        missedQuests.add(quest2);
        User updatedUser = userService.dismissMissedQuests(user, missedQuests);
        assertNotEquals(newLevel, updatedUser.getCharacterLevel());
    }

    @Test
    @DisplayName("Set next chapter on user on the last chapter should throw ValidationException")
    public void setNextChapter_userLastChapter_shouldThrowValidationException() {
        assertThrows(ValidationException.class, () ->
                userService.setNextStoryChapter(TestData.getDbUser3())
        );
    }

    @Test
    @DisplayName("Set next chapter on user but with not enough strength should throw ValidationException")
    public void setNextChapter_notEnoughStrength_shouldThrowValidationException() {
        assertThrows(ValidationException.class, () ->
                userService.setNextStoryChapter(TestData.getDbUser1())
        );
    }

    @Test
    @DisplayName("Set next chapter on user with enough strength does not throw")
    public void setNextChapter_enoughStrength_notThrows() {
        User u = TestData.getDbUser2();
        u.setCharacterStrength(200);
        userDao.updateUser(u);
        assertDoesNotThrow(() ->
                userService.setNextStoryChapter(u)
        );
        User _u = userService.getOneById(u.getId());
        assertEquals(TestData.getDbUser2().getStoryChapter() + 1, _u.getStoryChapter());
    }

    @Test
    @DisplayName("Check for update on story chapter on user with last chapter should return false")
    public void checkForNextChapter_userLastChapter_shouldReturnFalse() {
        assertFalse(userService.checkUserForNextStoryAndUpdate(TestData.getDbUser3()));
    }

    @Test
    @DisplayName("Check for update on story chapter on user with enough strength should return true")
    public void checkForNextChapter_userEnoughStrength_shouldReturnTrue() {
        User u = TestData.getDbUser5();
        u.setCharacterStrength(200);
        userDao.updateUser(u);
        assertTrue(userService.checkUserForNextStoryAndUpdate(u));
        User _u = userService.getOneById(u.getId());
        assertEquals(TestData.getDbUser5().getStoryChapter() + 1 ,_u.getStoryChapter());
    }

    @Test
    @DisplayName("Getting the leaderboard for a user should only get users with the same level")
    public void leaderboardForUser_allShouldHaveSameLevel() {
        User get = userService.getOneById(3);
        List<User> leaderboard = userService.getLeaderboardForUser(get);
        assertNotNull(leaderboard);
        for (User u: leaderboard) {
            assertEquals(get.getCharacterLevel(), u.getCharacterLevel());
        }
    }
}
