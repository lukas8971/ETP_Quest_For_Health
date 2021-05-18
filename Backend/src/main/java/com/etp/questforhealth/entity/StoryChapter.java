package com.etp.questforhealth.entity;

import java.util.Objects;

public class StoryChapter {
    private int id;
    private String name;
    private String description;
    private int strength_requirement;
    private Integer prev_chapter;
    private Integer next_chapter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStrength_requirement() {
        return strength_requirement;
    }

    public void setStrength_requirement(int strength_requirement) {
        this.strength_requirement = strength_requirement;
    }

    public Integer getPrev_chapter() {
        return prev_chapter;
    }

    public void setPrev_chapter(Integer prev_chapter) {
        this.prev_chapter = prev_chapter;
    }

    public Integer getNext_chapter() {
        return next_chapter;
    }

    public void setNext_chapter(Integer next_chapter) {
        this.next_chapter = next_chapter;
    }

    public StoryChapter() {}

    public StoryChapter(int id, String name, String description, int strength_requirement, Integer prev_chapter, Integer next_chapter) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.strength_requirement = strength_requirement;
        this.prev_chapter = prev_chapter;
        this.next_chapter = next_chapter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoryChapter that = (StoryChapter) o;
        return id == that.id
                && strength_requirement == that.strength_requirement
                && name.equals(that.name)
                && description.equals(that.description);
                /*&& prev_chapter.equals(that.prev_chapter)
                && next_chapter.equals(that.next_chapter);*/
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, strength_requirement, prev_chapter, next_chapter);
    }

    @Override
    public String toString() {
        return "StoryChapter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", strength_requirement=" + strength_requirement +
                ", prev_chapter=" + prev_chapter +
                ", next_chapter=" + next_chapter +
                '}';
    }
}
