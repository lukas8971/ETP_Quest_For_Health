package com.etp.questforhealth.base;

import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.entity.User;

import java.time.Duration;

/**
 * Interface to create the data to test the application
 */
public interface TestData {

    /**
     * URI Data
     */
    String BASE_URL = "http://localhost:";
    String QUEST_URL = "/quests";
    String USER_URL = "/users";

    /**
     * Quest Data
     */
    static Quest getNewQuest() { return new Quest(); }

    static Quest getNewStandardQuest(int id) { return new Quest(id, "Standard Test Quest", 10, 3); }

    static Quest getNewDoctorQuest(int id, int doctor) { return new Quest(id, "Doctor Test Quest", 20, 4, doctor); }

    /**
     * User Data
     */
    static User getNewUser() { return new User(); }

    static User getNewUserWithName(int id) { return new User(id, "Andrew", "Adams"); }
}
