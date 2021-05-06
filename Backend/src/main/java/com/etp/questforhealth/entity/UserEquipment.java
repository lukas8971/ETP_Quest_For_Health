package com.etp.questforhealth.entity;

import java.util.Objects;

public class UserEquipment {
    private int equipmentId;
    private int userId;

    public UserEquipment(){}

    public UserEquipment(int equipmentId, int userId){
        this.equipmentId = equipmentId;
        this.userId = userId;
    }

    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }
    public int getEquipmentId() { return equipmentId; }

    public void setUserId(int userId) { this.userId = userId; }
    public int getUserId() { return userId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEquipment userEquipment = (UserEquipment) o;
        return equipmentId == userEquipment.equipmentId &&
                userId == userEquipment.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipmentId, userId);
    }

    @Override
    public String toString(){
        return "UserEquipment{" +
                "equipmentId=" + equipmentId +
                ", userId='" + userId + "'" +
                '}';
    }
}
