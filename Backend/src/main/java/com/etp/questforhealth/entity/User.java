package com.etp.questforhealth.entity;

import java.util.Objects;

public class User {
    private int id;
    private String firstname;
    private String lastname;
    private String characterName;
    private int characterStrength;
    private int characterLevel;
    private int characterExp;
    private int characterGold;
    private String password;
    private String email;
    private int storyChapter;

    public User(){

    }

    public User(int id, String firstname, String lastname, String characterName, int characterStrength, int characterLevel, int characterExp, String password, String email, int storyChapter, int characterGold) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.characterName = characterName;
        this.characterStrength = characterStrength;
        this.characterLevel = characterLevel;
        this.characterExp = characterExp;
        this.characterGold = characterGold;
        this.password = password;
        this.email = email;
        this.storyChapter = storyChapter;
    }

    public User (int id, String firstname, String lastname){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getCharacterStrength() {
        return characterStrength;
    }

    public void setCharacterStrength(int characterStrength) {
        this.characterStrength = characterStrength;
    }

    public int getCharacterExp() {
        return characterExp;
    }

    public void setCharacterExp(int characterExp) {
        this.characterExp = characterExp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStoryChapter() {
        return storyChapter;
    }

    public void setStoryChapter(int storyChapter) {
        this.storyChapter = storyChapter;
    }

    public int getCharacterLevel() {
        return characterLevel;
    }

    public void setCharacterLevel(int characterLevel) {
        this.characterLevel = characterLevel;
    }

    public int getCharacterGold() {
        return characterGold;
    }

    public void setCharacterGold(int characterGold) {
        this.characterGold = characterGold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                characterStrength == user.characterStrength &&
                characterLevel == user.characterLevel &&
                characterExp == user.characterExp &&
                characterGold == user.characterGold &&
                storyChapter == user.storyChapter &&
                characterGold == user.characterGold &&
                Objects.equals(firstname, user.firstname) &&
                Objects.equals(lastname, user.lastname) &&
                Objects.equals(characterName, user.characterName) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, characterName, characterStrength, characterLevel, characterExp, password, email, storyChapter, characterGold);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", characterName='" + characterName + '\'' +
                ", characterStrength=" + characterStrength +
                ", characterLevel=" + characterLevel +
                ", characterExp=" + characterExp +
                ", characterGold=" + characterGold +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", storyChapter=" + storyChapter +
                '}';
    }
}
