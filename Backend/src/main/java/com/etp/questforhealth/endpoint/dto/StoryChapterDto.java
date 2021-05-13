package com.etp.questforhealth.endpoint.dto;

import java.util.Objects;

public class StoryChapterDto {
    private int id;
    private String name;
    private String description;
    private int strengthRequirement;
    private Integer prevChapter;
    private Integer nextChapter;

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

    public int getStrengthRequirement() {
        return strengthRequirement;
    }

    public void setStrengthRequirement(int strengthRequirement) {
        this.strengthRequirement = strengthRequirement;
    }

    public Integer getPrevChapter() {
        return prevChapter;
    }

    public void setPrevChapter(Integer prevChapter) {
        this.prevChapter = prevChapter;
    }

    public Integer getNextChapter() {
        return nextChapter;
    }

    public void setNextChapter(Integer next_chapter) {
        this.nextChapter = nextChapter;
    }

    public StoryChapterDto() {}

    public StoryChapterDto(int id, String name, String description, int strengthRequirement, Integer prevChapter, Integer nextChapter) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.strengthRequirement = strengthRequirement;
        this.prevChapter = prevChapter;
        this.nextChapter = nextChapter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoryChapterDto that = (StoryChapterDto) o;
        return id == that.id
                && strengthRequirement == that.strengthRequirement
                && name.equals(that.name)
                && description.equals(that.description);
                /*&& prev_chapter.equals(that.prev_chapter)
                && next_chapter.equals(that.next_chapter);*/
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, strengthRequirement, prevChapter, nextChapter);
    }

    @Override
    public String toString() {
        return "StoryChapterDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", strengthRequirement=" + strengthRequirement +
                ", prevChapter=" + prevChapter +
                ", nextChapter=" + nextChapter +
                '}';
    }
}
