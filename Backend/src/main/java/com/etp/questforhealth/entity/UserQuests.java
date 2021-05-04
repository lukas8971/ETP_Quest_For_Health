package com.etp.questforhealth.entity;

import java.util.List;
import java.util.Objects;

public class UserQuests {
    private User user;
    private List<Quest> quests;

    public UserQuests(User user, List<Quest> quest) {
        this.user = user;
        this.quests = quest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public void setQuests(List<Quest> quests) {
        this.quests = quests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserQuests userQuests = (UserQuests) o;
        return Objects.equals(user, userQuests.user) &&
                Objects.equals(quests, userQuests.quests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, quests);
    }

    @Override
    public String toString() {
        return "UserQuest{" +
                "user=" + user.toString() +
                ", quests=" + quests.toString() +
                '}';
    }
}
