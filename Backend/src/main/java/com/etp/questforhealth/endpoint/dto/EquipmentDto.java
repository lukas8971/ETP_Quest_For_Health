package com.etp.questforhealth.endpoint.dto;

import com.etp.questforhealth.entity.Equipment;
import com.etp.questforhealth.entity.enums.EquipmentType;

import java.util.Objects;

public class EquipmentDto {
    private int id;
    private String name;
    private String description;
    private int price;
    private int strength;
    private EquipmentType type;

    public EquipmentDto(int id, String name, String description, int price, int strength, EquipmentType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.strength = strength;
        this.type = type;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipmentDto equipment = (EquipmentDto) o;
        return id == equipment.id &&
                price == equipment.price &&
                strength == equipment.strength &&
                Objects.equals(name, equipment.name) &&
                Objects.equals(description, equipment.description) &&
                type == equipment.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, strength, type);
    }
}
