package com.etp.questforhealth.endpoint.dto;

import java.time.LocalDate;
import java.util.Objects;

public class CompletedQuestDto {
    private int quest;
    private int user;
    private LocalDate completedOn;
    boolean completed;

    public CompletedQuestDto(){}

    public CompletedQuestDto(int quest, int user, LocalDate completedOn, boolean completed) {
        this.quest = quest;
        this.user = user;
        this.completedOn = completedOn;
        this.completed = completed;
    }

    public int getQuest() {
        return quest;
    }

    public void setQuest(int quest) {
        this.quest = quest;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public LocalDate getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(LocalDate completedOn) {
        this.completedOn = completedOn;
    }

    public boolean isCompleted() { return completed; }

    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletedQuestDto that = (CompletedQuestDto) o;
        return quest == that.quest && user == that.user && completed == that.completed && Objects.equals(completedOn, that.completedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quest, user, completedOn, completed);
    }

    @Override
    public String toString() {
        return "CompletedQuestDto{" +
                "quest=" + quest +
                ", user=" + user +
                ", completedOn=" + completedOn +
                ", completed=" + completed +
                '}';
    }
}
