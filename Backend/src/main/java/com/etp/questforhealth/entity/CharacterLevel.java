package com.etp.questforhealth.entity;

import java.util.Objects;

public class CharacterLevel {
    private int id;
    private int total_strength;
    private int needed_exp;
    private int level;
    private String rank;

    public CharacterLevel(int id, int total_strength, int needed_exp, int level, String rank) {
        this.id = id;
        this.total_strength = total_strength;
        this.needed_exp = needed_exp;
        this.level = level;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal_strength() {
        return total_strength;
    }

    public void setTotal_strength(int total_strength) {
        this.total_strength = total_strength;
    }

    public int getNeeded_exp() {
        return needed_exp;
    }

    public void setNeeded_exp(int needed_exp) {
        this.needed_exp = needed_exp;
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
        CharacterLevel that = (CharacterLevel) o;
        return id == that.id &&
                total_strength == that.total_strength &&
                needed_exp == that.needed_exp &&
                level == that.level &&
                Objects.equals(rank, that.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, total_strength, needed_exp, level, rank);
    }
}
