package com.etp.questforhealth.endpoint.dto;

import java.util.Objects;

public class CreateDoctorQuestDto {

    private CredentialsDto credentials;
    private QuestDto quest;

    public CreateDoctorQuestDto(){}

    public CreateDoctorQuestDto(CredentialsDto credentials, QuestDto quest) {
        this.credentials = credentials;
        this.quest = quest;
    }

    public CredentialsDto getCredentials() {
        return credentials;
    }

    public void setCredentials(CredentialsDto credentials) {
        this.credentials = credentials;
    }

    public QuestDto getQuest() {
        return quest;
    }

    public void setQuest(QuestDto quest) {
        this.quest = quest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateDoctorQuestDto that = (CreateDoctorQuestDto) o;
        return Objects.equals(credentials, that.credentials) && Objects.equals(quest, that.quest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentials, quest);
    }

    @Override
    public String toString() {
        return "CreateDoctorQuestDto{" +
                "credentials=" + credentials +
                ", quest=" + quest +
                '}';
    }
}
