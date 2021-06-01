import {Component, Input, OnInit} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Equipment} from '../../dto/equipment';
import {ErrorDialogComponent} from '../error-dialog/error-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {EquipmentService} from '../../service/equipment.service';
import {UserEquipment} from '../../dto/user-equipment';
import {MatSnackBar} from '@angular/material/snack-bar';
import {UserService} from '../../service/user.service';
import {HeaderInfoService} from '../../service/header-info.service';
import {User} from '../../dto/user';
import {faCoins, faDiceD20, faFistRaised, faScroll} from '@fortawesome/free-solid-svg-icons';
import {MessagesService} from "../../service/message-service";

@Component({
  selector: 'app-equipment-component',
  templateUrl: './equipment-component.component.html',
  styleUrls: ['./equipment-component.component.css']
})
export class EquipmentComponentComponent implements OnInit {

  /**
   * Input to select the equipment type
   */
  @Input() type: string | undefined;


  userId = -1;
  equipment: any[] = [];
  selectedEquipment: any;
  equipmentColumns = ['name', 'price', 'strength'];
  dataSource = new MatTableDataSource(this.equipment);

  faCoins = faCoins;
  faStrength = faFistRaised;
  faExp = faDiceD20;
  faStory = faScroll;

  constructor(private dialog: MatDialog, private equipmentService: EquipmentService, private snackBar: MatSnackBar,
              private userService: UserService, private headerInfoService: HeaderInfoService, private messageService: MessagesService) {
    this.userId = Number(sessionStorage.getItem('userId'));
  }

  ngOnInit(): void {
    this.getAvailableEquipment();
  }

  /**
   * Filter for the patients table
   * @param event the given filter
   */
  public applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  /**
   * Opens sidebar to view the clicked equipment
   * @param eq that is clicked on
   */
  public selectEquipment(eq: Equipment): void{
    console.log('Selected ' + eq.name);
    this.selectedEquipment = eq;
  }

  /**
   * Buys the equipment (if enough gold)
   * @param eqId of the equipment to buy
   */
  public buy(eqId: number): void{
    console.log('Buy equipment ' + eqId);
    const ue = new UserEquipment(eqId, this.userId);
    this.buyNewEquipment(ue, false);
  }

  /**
   * Buys and equips the equipment (if enough gold)
   * @param eqId of the equipment to buy and equip
   */
  public buyAndEquip(eqId: number): void{
    console.log('Buy and equip ' + eqId);
    const ue = new UserEquipment(eqId, this.userId);
    this.buyNewEquipment(ue, true);
  }

  /**
   * Buys a new item
   * @param ue a UserEquipment object that should be bought
   * @param equip true if the new item should also be equipped after its bought
   */
  private buyNewEquipment(ue: UserEquipment, equip: boolean): void{
    console.log('Buy new equipment');
    this.equipmentService.buyNewEquipment(ue).subscribe(
      (eq: Equipment) => {
        this.getAvailableEquipment();
        this.snackBar.open('You now own ' + eq.name + '!', 'Great');
        this.selectedEquipment = null;
        if (equip){
          this.equipNewEquipment(eq);
        }
        this.messageService.sendMessage({ message: 'gold_changed', receiver: 'all'});
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
    this.userService.checkUserForNextStoryAndUpdate(this.userId).subscribe(
      (up: boolean) => {
        if (up) {
          this.snackBar.open('Great. You got to the next chapter!', 'Yasss');
        }
        this.getUserForHeader();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Gets the current user for the header info component
   */
  private getUserForHeader(): void {
    console.log('getUserForHeader');
    this.userService.getUserById(this.userId).subscribe(
      (user: User) => {
        this.headerInfoService.setUser(user);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Equips the new item after buying
   * @param e equipment that should be equipped
   */
  private equipNewEquipment(e: Equipment): void{
    console.log('Equip new equipment');
    this.equipmentService.equipItem(this.userId, e).subscribe(
      (eq: Equipment) => {
        this.snackBar.open('You now own and wear ' + eq.name + '!', 'Great');
        this.checkUserForNextStoryAndUpdate();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Gets all the available equipments of type eqType
   */
  private getAvailableEquipment(): void{
    console.log('Get available character equipment');
    this.equipmentService.getAvailableEquipmentByTypeAndId(String(this.type), this.userId).subscribe(
      (eq: Equipment[]) => {
        this.equipment = eq;
        this.dataSource = new MatTableDataSource(this.equipment);
        this.selectedEquipment = eq[0];
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Shows an error message if an error occurs
   * @param error that should be displayed
   */
  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    this.dialog.open(ErrorDialogComponent, {
      data: { err: error, message: '' }
    });
  }
}
