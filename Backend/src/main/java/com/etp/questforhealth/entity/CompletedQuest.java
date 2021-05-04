package com.etp.questforhealth.entity;

import java.time.LocalDate;
import java.util.Objects;

public class CompletedQuest {

    private int quest;
    private int user;
    private LocalDate completedOn;
    // boolean completed

    public CompletedQuest(){}

    public CompletedQuest(int quest, int user, LocalDate completedOn) {
        this.quest = quest;
        this.user = user;
        this.completedOn = completedOn;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletedQuest that = (CompletedQuest) o;
        return quest == that.quest && user == that.user && Objects.equals(completedOn, that.completedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quest, user, completedOn);
    }

    @Override
    public String toString() {
        return "CompletedQuest{" +
                "quest=" + quest +
                ", user=" + user +
                ", completedOn=" + completedOn +
                '}';
    }
}
