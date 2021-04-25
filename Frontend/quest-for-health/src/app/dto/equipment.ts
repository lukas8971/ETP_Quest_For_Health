enum EquipmentType{
  HEAD,
  ARMS,
  TORSO,
  LEGS,
  RIGHT_HAND,
  LEFT_HAND

}

export interface Equipment{
  id: number;
  name: string;
  description: string;
  price: number;
  strength: number;
  type: EquipmentType;
}
