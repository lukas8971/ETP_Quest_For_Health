import { Component, OnInit } from '@angular/core';
import {User} from '../../dto/user';
import {UserService} from '../../service/user.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {CharacterLevelService} from '../../service/character-level.service';
import {CharacterLevel} from '../../dto/character-level';
import {EquipmentService} from '../../service/equipment.service';
import {Equipment} from '../../dto/equipment';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  user: any;
  nextLevel: any;
  neededExp = 0;
  userEquipment: any;
  headEquipment: any;
  torsoEquipment: any;
  armsEquipment: any;
  rightHandEquipment: any;
  leftHandEquipment: any;
  legsEquipment: any;
  currentLevel: any;

  head = false;
  torso = false;
  arms = false;
  legs = false;
  rHand = false;
  lHand = false;

  strength = 0;

  constructor(private equipmentService: EquipmentService, private chararacterLevelService: CharacterLevelService,
              private userService: UserService, private snackBar: MatSnackBar) {
  }

  loadUser(): void{
    this.userService.getUserById(Number(sessionStorage.getItem('userId'))).subscribe(
      (u: User) => {
        this.user =  u;
        this.loadNextLevel();
        this.loadUserEquipment();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  loadNextLevel(): void{
    this.chararacterLevelService.getCharacterLevelById(this.user.characterLevel).subscribe(
      (c: CharacterLevel) => {
        this.chararacterLevelService.getCharacterLevelByLevel(c.level + 1).subscribe(
          (d: CharacterLevel) => {
            this.nextLevel = d;
            this.neededExp = d.neededExp - this.user.characterExp;
            this.currentLevel = c;

          }
        );
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  loadUserEquipment(): void{
    this.equipmentService.getEquipmentWornByUserId(this.user.id).subscribe(
      (e: Equipment[]) => {
        this.userEquipment = e;
        this.assignEquipment();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Opens a new component with all available equipments to change
   * @param type of the equipment
   */
  public changeEquipment(type: string): void{
    console.log('changeEquipment(' + type + ')');
    this.head = false;
    this.torso = false;
    this.arms = false;
    this.legs = false;
    this.rHand = false;
    this.lHand = false;
    if (type === 'head') { this.head = true; }
    else if (type === 'torso') { this.torso = true; }
    else if (type === 'arms') { this.arms = true; }
    else if (type === 'right hand') { this.rHand = true; }
    else if (type === 'left hand') { this.lHand = true; }
    else if (type === 'legs') { this.legs = true; }
  }

  /**
   * Called when one of the children changed
   */
  public childChanged(): void{
    console.log('Child changed!');
    this.loadUser();
  }

  /**
   * Calculates the strength of a character
   */
  public calculateStrength(): void{
    console.log('Calculate strength');
    this.userService.getUserStrength(this.user.id).subscribe(
      (s: number) => {
        this.strength = s;
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  private getEquipmentOfType(type: string): Equipment{
    return this.userEquipment.find((item: { type: string; }) => item.type === type);
  }
  private assignEquipment(): void {
    this.headEquipment = this.getEquipmentOfType('HEAD');
    this.armsEquipment = this.getEquipmentOfType('ARMS');
    this.torsoEquipment = this.getEquipmentOfType('TORSO');
    this.rightHandEquipment = this.getEquipmentOfType('RIGHT_HAND');
    this.leftHandEquipment = this.getEquipmentOfType('LEFT_HAND');
    this.legsEquipment = this.getEquipmentOfType('LEGS');
    this.calculateStrength();
  }

  ngOnInit(): void {
    this.loadUser();
  }



  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    this.snackBar.open('Could not load profile details, please try again later', 'Ok');
  }

}
