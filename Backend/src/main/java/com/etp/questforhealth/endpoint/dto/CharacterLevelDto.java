package com.etp.questforhealth.endpoint.dto;

import java.util.Objects;

public class CharacterLevelDto {
    private int id;
    private int totalStrength;
    private int neededExp;
    private int level;
    private String rank;

    public CharacterLevelDto(int id, int total_strength, int needed_exp, int level, String rank) {
        this.id = id;
        this.totalStrength = total_strength;
        this.neededExp = needed_exp;
        this.level = level;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalStrength() {
        return totalStrength;
    }

    public void setTotalStrength(int totalStrength) {
        this.totalStrength = totalStrength;
    }

    public int getNeededExp() {
        return neededExp;
    }

    public void setNeededExp(int neededExp) {
        this.neededExp = neededExp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterLevelDto that = (CharacterLevelDto) o;
        return id == that.id &&
                totalStrength == that.totalStrength &&
                neededExp == that.neededExp &&
                level == that.level &&
                Objects.equals(rank, that.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalStrength, neededExp, level, rank);
    }
}
