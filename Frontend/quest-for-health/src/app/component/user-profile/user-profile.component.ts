import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import {User} from '../../dto/user';
import {UserService} from '../../service/user.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {CharacterLevelService} from '../../service/character-level.service';
import {CharacterLevel} from '../../dto/character-level';
import {EquipmentService} from '../../service/equipment.service';
import {Equipment} from '../../dto/equipment';
import {HeaderInfoService} from '../../service/header-info.service';
import {faCoins, faDiceD20, faFistRaised, faScroll} from '@fortawesome/free-solid-svg-icons';
import {StoryChapter} from '../../dto/story-chapter';
import {StoryService} from '../../service/story.service';
import {MessagesService} from "../../service/message-service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-user-profile',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  private messagesServiceSubscription: Subscription;

  user: any;
  nextLevel: any;
  neededExp = 0;
  neededSt = 0;
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

  faCoins = faCoins;
  faStrength = faFistRaised;
  faExp = faDiceD20;
  faStory = faScroll;

  strength = 0;

  constructor(private equipmentService: EquipmentService, private chararacterLevelService: CharacterLevelService,
              private userService: UserService, private snackBar: MatSnackBar, private headerInfoService: HeaderInfoService,
              private storyService: StoryService, private messagesService: MessagesService) {
    this.messagesServiceSubscription = this.messagesService.subscribeToMessagesChannel().subscribe(
      message => {
        console.log('Received message from messageservice: ' + message.message);
        if (message.receiver === 'all' || message.receiver === 'UserProfile') {
          if (message.message === 'equipment_changed') {
            this.loadUserEquipment();
            this.loadUser();
          }
        }
      }
    );

  }

  // tslint:disable-next-line:use-lifecycle-interface
  public ngOnChanges(...args: any[]): void {
    console.log('Change equipment');
    if (this.user !== undefined && this.user.id > 0) {
      this.loadUserEquipment();
    }
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

  loadNextStory(): void {
    console.log('Load next chapter info');
    this.storyService.getNextChapterInfoOfUser(this.user.id).subscribe(
      (sto: StoryChapter) => {
        this.neededSt = sto.strengthRequirement - this.strength;
      }, error => {
        // this.defaultServiceErrorHandling(error);
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
        this.checkUserForNextStoryAndUpdate();
        this.loadNextStory();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }


  /**
   * Checks the chapter of a user and updates it if the strength is high enough
   */
  private checkUserForNextStoryAndUpdate(): void {
    console.log('checkUserForNextStoryAndUpdate');
    this.userService.checkUserForNextStoryAndUpdate(this.user.id).subscribe(
      (up: boolean) => {
        if (up) {
          this.snackBar.open('Great. You got to the next chapter!', 'Yasss');
        }
        //this.getUserForHeader();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Gets the user for the header info component
   */
  /*
  private getUserForHeader(): void {
    console.log('getUserForHeader');
    this.userService.getUserById(this.user.id).subscribe(
      (user: User) => {
        this.headerInfoService.setUser(user);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }
  */

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
