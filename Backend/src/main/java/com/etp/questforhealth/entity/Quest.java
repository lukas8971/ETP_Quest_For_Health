package com.etp.questforhealth.entity;


import java.time.Duration;
import java.util.Objects;

public class Quest {

    private int id;
    private String name;
    private String description;
    private int exp_reward;
    private int gold_reward;
    private Duration repetition_cycle;
    private int exp_penalty;
    private int gold_penalty;
    private int doctor;

    public Quest(){}

    public Quest(int id, String name, String description, int exp_reward, int gold_reward, Duration repetition_cycle, int exp_penalty, int gold_penalty, int doctor) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exp_reward = exp_reward;
        this.gold_reward = gold_reward;
        this.repetition_cycle = repetition_cycle;
        this.exp_penalty = exp_penalty;
        this.gold_penalty = gold_penalty;
        this.doctor = doctor;
    }

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

    public int getExp_reward() {
        return exp_reward;
    }

    public void setExp_reward(int exp_reward) {
        this.exp_reward = exp_reward;
    }

    public int getGold_reward() {
        return gold_reward;
    }

    public void setGold_reward(int gold_reward) {
        this.gold_reward = gold_reward;
    }

    public Duration getRepetition_cycle() {
        return repetition_cycle;
    }

    public void setRepetition_cycle(Duration repetition_cycle) {
        this.repetition_cycle = repetition_cycle;
    }

    public int getExp_penalty() {
        return exp_penalty;
    }

    public void setExp_penalty(int exp_penalty) {
        this.exp_penalty = exp_penalty;
    }

    public int getGold_penalty() {
        return gold_penalty;
    }

    public void setGold_penalty(int gold_penalty) {
        this.gold_penalty = gold_penalty;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quest)) return false;
        Quest quest = (Quest) o;
        return id == quest.id && exp_reward == quest.exp_reward && gold_reward == quest.gold_reward && exp_penalty == quest.exp_penalty
                && gold_penalty == quest.gold_penalty && doctor == quest.doctor && Objects.equals(name, quest.name)
                && Objects.equals(description, quest.description) && Objects.equals(repetition_cycle, quest.repetition_cycle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, exp_reward, gold_reward, repetition_cycle, exp_penalty, gold_penalty, doctor);
    }

    @Override
    public String toString() {
        return "Quest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", exp_reward=" + exp_reward +
                ", gold_reward=" + gold_reward +
                ", repetition_cycle=" + repetition_cycle +
                ", exp_penalty=" + exp_penalty +
                ", gold_penalty=" + gold_penalty +
                ", doctor=" + doctor +
                '}';
    }
}