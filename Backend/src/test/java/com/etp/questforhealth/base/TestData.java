package com.etp.questforhealth.base;

import com.etp.questforhealth.endpoint.dto.*;
import com.etp.questforhealth.entity.*;
import com.etp.questforhealth.entity.enums.EquipmentType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
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
     * CredentialsDto Data
     */

    static CredentialsDto getNewCredentialsDto(){return new CredentialsDto("e.m@il.com","QWERT1234");}

    /**
    * Credentials Data
    */

    static Credentials getNewCredentials(){return new Credentials("e.m@il.com","QWERT1234");}



    /**
     * CreateQuestDto Data
     */

    static CreateDoctorQuestDto getNewCreateDoctorQuestDto(){return new CreateDoctorQuestDto(getNewCredentialsDto(), getNewQuestDto());}

    /**
     *  CreateDoctorQuest Data
     */

    static CreateDoctorQuest getNewCreateDoctorQuest(){return new CreateDoctorQuest(getNewCredentials(),getNewDoctorQuest());}

    /**
     * QuestDto Data
     */

    static QuestDto getNewQuestDto(){return new QuestDto(0,"UnitQuest","This Quest was created by a Unit Test", 100,10, Duration.parse("PT0S"),0,0,1, new Date());}



    /**
     * Quest Data
     */
    static Quest getNewQuest() { return new Quest(); }

    static Quest getNewStandardQuest(int id) { return new Quest(id, "Standard Test Quest", 10, 3); }

    static Quest getNewDoctorQuest(int id, int doctor) { return new Quest(id, "Doctor Test Quest", 20, 4, doctor); }

    static Quest getNewDoctorQuest() {return new Quest(0,"UnitQuest","This Quest was created by a Unit Test", 100,10, Duration.parse("PT0S"),0,0,1, new Date());}





    /**
     * User Data
     */
    static User getNewUser() { return new User(); }

    static User getNewUserWithName(int id) { return new User(id, "Andrew", "Adams"); }

    static User getNewWorkingUser(){return new User(0,"Hans-Peter", "Berger", "Rudolf", 0,1,400,"SuperSecretPassword", null,1,0); }

    static User getNewWorkingUserDifferentCharacter(){return new User(0,"H.P.", "Baxxter", "Scooter", 9000,1,400,"OnlyIKnowHowMuchTheFishIs", null,1, 0); }

    static User getNewWorkingUserDifferentCharacter2(){return new User(0,"H.P.", "Baxxter", "Scooter2", 9000,1,400,"OnlyIKnowHowMuchTheFishIs", null,1, 0); }

    static User getNewWorkingUserDifferentCharacter3(){return new User(0,"H.P.", "Baxxter", "Scooter3", 9000,1,400,"OnlyIKnowHowMuchTheFishIs", null,1, 0); }

    static User getNewWorkingUserDifferentCharacter4(){return new User(0,"H.P.", "Baxxter", "Scooter4", 9000,1,400,"OnlyIKnowHowMuchTheFishIs", null,1, 0); }

    static User getNewWorkingUserDifferentCharacter5(){return new User(0,"H.P.", "Baxxter", "Scooter5", 9000,1,400,"OnlyIKnowHowMuchTheFishIs", null,1, 0); }

    static User getNewWorkingUserDifferentCharacter6(){return new User(0,"H.P.", "Baxxter", "Scooter6", 9000,1,400,"OnlyIKnowHowMuchTheFishIs", null,1, 0); }

    static List<User> getNWorkingUsers(int n){
        List<User> retVal = new ArrayList<User>();
        for(int i=0; i<n; i++){
            retVal.add(new User(0,"Hans-Peter"+i, "Berger"+i, "Rudolf"+i, 0,1,400,"SuperSecretPassword"+i, null,1,i));
        }
        return retVal;
    }


    /**
     * Equipment Data
     */
    static Equipment getNewEquipmentWOid(int price, int strength, EquipmentType type){
        return new Equipment("UnitTest Equipment", "This should never be worn!", price, strength, type);
    }

    /**
     * Doctor Data
     */
    static Doctor getExistingDoctorTestDoc1() {
        return new Doctor(1, "Test", "Doc", "e.m@il.com", "QWERT1234");
    }

    static Doctor getExistingDoctorGeraldSteinhardt2() {
        return new Doctor(2, "Gerald", "Steinhardt", "sig@na.nz", "BesterProf");
    }

    static Doctor getExistingDoctorAntonAlfred3() {
        return new Doctor(3, "Anton", "Alfred", "a-a@doc.at", "pass");
    }

    static Doctor getNotExistingDoctor4() {
        return new Doctor(4, "Wrong", "Doctor", "wrong@doc.tor", "hardToGuess");
    }
}
