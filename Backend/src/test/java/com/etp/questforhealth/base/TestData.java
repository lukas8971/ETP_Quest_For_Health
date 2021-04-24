package com.etp.questforhealth.base;

import com.etp.questforhealth.entity.Quest;
import com.etp.questforhealth.entity.User;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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

    static User getNewWorkingUser(){return new User(0,"Hans-Peter", "Berger", "Rudolf", 0,1,400,"SuperSecretPassword", null,1); }

    static List<User> getNWorkingUsers(int n){
        List<User> retVal = new ArrayList<User>();
        for(int i=0; i<n; i++){
            retVal.add(new User(0,"Hans-Peter"+i, "Berger"+i, "Rudolf"+i, 0,1,400,"SuperSecretPassword"+i, null,1));
        }
        return retVal;
    }
}
