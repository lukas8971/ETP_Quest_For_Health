package com.etp.questforhealth.entity;

import java.util.Objects;

public class CreateDoctorQuest {

    private Credentials credentials;
    private Quest quest;

    private CreateDoctorQuest(){}

    public CreateDoctorQuest(Credentials credentials, Quest quest) {
        this.credentials = credentials;
        this.quest = quest;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateDoctorQuest that = (CreateDoctorQuest) o;
        return Objects.equals(credentials, that.credentials) && Objects.equals(quest, that.quest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentials, quest);
    }

    @Override
    public String toString() {
        return "CreateDoctorQuest{" +
                "credentials=" + credentials +
                ", quest=" + quest +
                '}';
    }
}
