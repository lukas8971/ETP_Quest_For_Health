package com.etp.questforhealth.unit.service;

import com.etp.questforhealth.base.DatabaseTestData;
import com.etp.questforhealth.base.TestData;
import com.etp.questforhealth.entity.StoryChapter;
import com.etp.questforhealth.entity.User;
import com.etp.questforhealth.exception.ValidationException;
import com.etp.questforhealth.persistence.UserDao;
import com.etp.questforhealth.service.StoryChapterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
public class StoryChapterServiceTest {

    @Autowired
    StoryChapterService storyService;
    @Autowired
    UserDao userDao;

    @BeforeAll
    public static void testData(){
        DatabaseTestData.insertTestData();
    }

    @Test
    @DisplayName("Get previous chapter of first chapter should return ValidationException")
    public void getPrevious_ofFirst_shouldReturnValidationException() {
        assertThrows(ValidationException.class, () ->
                storyService.getPrevChapter(TestData.getFirstStory())
        );
    }

    @Test
    @DisplayName("Get next chapter of last chapter should return ValidationException")
    public void getNext_ofLast_shouldReturnValidationException() {
        assertThrows(ValidationException.class, () ->
                storyService.getNextChapter(TestData.getThirdChapter())
        );
    }

    @Test
    @DisplayName("Get next chapter of middle chapter should return the next chapter")
    public void getNext_ofMiddle_shouldReturnNext() {
        StoryChapter[] x = new StoryChapter[1];
        assertDoesNotThrow(() ->
                x[0] = storyService.getNextChapter(TestData.getSecondChapter())
        );
        assertEquals(TestData.getThirdChapter(), x[0]);
        assertNull(x[0].getNext_chapter());
    }

    @Test
    @DisplayName("Get previous chapter of middle chapter should return the previous chapter")
    public void getPrevious_ofMiddle_shouldReturnPrevious() {
        StoryChapter[] x = new StoryChapter[1];
        assertDoesNotThrow(() ->
                x[0] = storyService.getPrevChapter(TestData.getSecondChapter())
        );
        assertEquals(TestData.getFirstStory(), x[0]);
        assertNull(x[0].getPrev_chapter());
    }


    @Test
    @DisplayName("Get all previous chapters of 3 should return a list of 3")
    public void getAllPrev_shouldReturnListOf3() {
        List<StoryChapter> list = storyService.getAllPrevChapters(TestData.getDbUser3());
        assertEquals(3, list.size());
        assertEquals(list.get(0), TestData.getFirstStory());
        assertEquals(list.get(1), TestData.getSecondChapter());
        assertEquals(list.get(2), TestData.getThirdChapter());
    }

    @Test
    @DisplayName("Get all next chapters of 1 should return a list of 2")
    public void getAllNext_shouldReturnListOf2() {
        List<StoryChapter> list = storyService.getAllNextChapters(TestData.getDbUser1());
        assertEquals(2, list.size());
        for (StoryChapter sc: list) {
            System.out.println(sc);
        }
        assertEquals(list.get(0), TestData.getSecondChapter());
        assertEquals(list.get(1), TestData.getThirdChapter());
    }

    @Test
    @DisplayName("Next user chapter of last story should return ValidationException")
    public void nextUserChapter_ofLast_shouldThrowValidationException() {
        assertThrows(ValidationException.class,() ->
            storyService.getNextStoryOfUser(TestData.getDbUser3())
        );
    }

    @Test
    @DisplayName("Next user chapter of middle story should return last chapter")
    public void nextUserChapter_ofMiddle_shouldreturnLastChapter() {
        StoryChapter[] x = new StoryChapter[1];
        User u = TestData.getDbUser2();
        u.setCharacterStrength(200);
        userDao.updateUser(u);
        assertDoesNotThrow(() ->
                x[0] = storyService.getNextStoryOfUser(u)
        );
        assertEquals(TestData.getThirdChapter(), x[0]);
    }

    @Test
    @DisplayName("Prev user chapter of middle story should return first chapter")
    public void prevUserChapter_ofMiddle_shouldreturnFirstChapter() {
        StoryChapter[] x = new StoryChapter[1];
        assertDoesNotThrow(() ->
                x[0] = storyService.getPrevStoryOfUser(TestData.getDbUser2())
        );
        assertEquals(TestData.getFirstStory(), x[0]);
    }

    @Test
    @DisplayName("Prev user chapter of middle story should return first chapter")
    public void prevUserChapter_ofFirst_shouldThrowValidationException() {
        assertThrows(ValidationException.class, () ->
                storyService.getPrevStoryOfUser(TestData.getDbUser1())
        );
    }
}
