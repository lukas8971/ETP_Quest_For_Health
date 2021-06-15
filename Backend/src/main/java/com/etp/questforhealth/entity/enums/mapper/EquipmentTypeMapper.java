package com.etp.questforhealth.entity.enums.mapper;

import com.etp.questforhealth.entity.enums.EquipmentType;

public class EquipmentTypeMapper {
    public static EquipmentType stringToEnum(String s){
        switch(s.toLowerCase()){
            case "head": return EquipmentType.HEAD;
            case "arms": return EquipmentType.ARMS;
            case "torso": return EquipmentType.TORSO;
            case "legs": return EquipmentType.LEGS;
            case "right hand": return EquipmentType.RIGHT_HAND;
            case "left hand": return EquipmentType.LEFT_HAND;
        }
        throw new IllegalArgumentException(s+ " is not a valid Equipment-type");
    }

    /**
     * Turns the enum value to a string
     * @param e EquipmentType enum that should be converted to a string
     * @return the string value of the enum
     */
    public static String enumToString(EquipmentType e){
        String s = e.name();
        return s.replace('_', ' ');
    }
}
