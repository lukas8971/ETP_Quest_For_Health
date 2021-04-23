package com.etp.questforhealth.entity;

import java.time.LocalDate;
import java.util.Objects;

public class AcceptedQuest {
    private int quest;
    private int user;
    private LocalDate acceptedOn;

    public AcceptedQuest(){}

    public AcceptedQuest(int quest, int user, LocalDate acceptedOn){
        this.quest = quest;
        this.user = user;
        this.acceptedOn = acceptedOn;
    }

    public void setQuest(int quest) { this.quest = quest; }
    public int getQuest() { return quest; }

    public void setUser(int user) { this.user = user; }
    public int getUser() { return user; }

    public void setAcceptedOn(LocalDate acceptedOn) { this.acceptedOn = acceptedOn; }
    public LocalDate getAcceptedOn() { return acceptedOn; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcceptedQuest acceptedQuest = (AcceptedQuest) o;
        return quest == acceptedQuest.quest &&
                Objects.equals(user, acceptedQuest.getUser()) &&
                Objects.equals(acceptedOn, acceptedQuest.acceptedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quest, user, acceptedOn);
    }

    @Override
    public String toString() {
        return "AcceptedQuest{" +
                "quest=" + quest +
                ", user='" + user + "'" +
                ", acceptedOn='" + acceptedOn + "'" +
                '}';
    }
}
