import { Component, OnInit } from '@angular/core';
import {User} from "../../dto/user";
import {UserService} from "../../service/user.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CharacterLevelService} from "../../service/character-level.service";
import {CharacterLevel} from "../../dto/character-level";
import {EquipmentService} from "../../service/equipment.service";
import {Equipment} from "../../dto/equipment";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  user: any;
  nextLevel: any;
  rank: string="";
  neededExp=0;
  userEquipment: any;
  headEquipment: any;
  torsoEquipment: any;
  armsEquipment: any;
  rightHandEquipment: any;
  leftHandEquipment: any;
  legsEquipment: any;

  constructor(private equipmentService: EquipmentService, private chararacterLevelService: CharacterLevelService,private userService: UserService, private snackBar: MatSnackBar) {
  }

  loadUser(): void{
    this.userService.getUserById(Number(sessionStorage.getItem('userId'))).subscribe(
      (u:User) => {
        this.user =  u;
        this.loadNextLevel();
        this.loadUserEquipment();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )
  }

  loadNextLevel(): void{
    this.chararacterLevelService.getCharacterLevelById(this.user.characterLevel).subscribe(
      (c: CharacterLevel) => {
        this.chararacterLevelService.getCharacterLevelByLevel(c.level +1).subscribe(
          (d: CharacterLevel) => {
            this.nextLevel = d;
            this.neededExp =d.neededExp-this.user.characterExp;
            this.rank = c.rank;
          }
        )
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )
  }

  loadUserEquipment(): void{
    this.equipmentService.getEquipmentWornByUserId(this.user.id).subscribe(
      (e: Equipment[]) => {
        this.userEquipment = e;
        this.assignEquipment();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )
  }
  private getEquipmentOfType (type: string): Equipment{
    return this.userEquipment.find((item: { type: string; }) => item.type === type)
  }
  private assignEquipment(): void {
    this.headEquipment = this.getEquipmentOfType('HEAD');
    this.armsEquipment = this.getEquipmentOfType('ARMS');
    this.torsoEquipment=this.getEquipmentOfType('TORSO');
    this.rightHandEquipment=this.getEquipmentOfType("RIGHT_HAND");
    this.leftHandEquipment=this.getEquipmentOfType('LEFT_HAND');
    this.legsEquipment=this.getEquipmentOfType('LEGS');

  }

  ngOnInit(): void {
    this.loadUser();
  }



  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    this.snackBar.open('Could not load profile details, please try again later', 'Ok');
  }

}
