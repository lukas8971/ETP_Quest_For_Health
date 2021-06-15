package com.etp.questforhealth.entity;

import java.util.Objects;

public class UserQuest {
    private User user;
    private Quest quest;

    public UserQuest(User user, Quest quest) {
        this.user = user;
        this.quest = quest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        UserQuest userQuest = (UserQuest) o;
        return Objects.equals(user, userQuest.user) &&
                Objects.equals(quest, userQuest.quest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, quest);
    }

    @Override
    public String toString() {
        return "UserQuest{" +
                "user=" + user.toString() +
                ", quest=" + quest.toString() +
                '}';
    }
}
