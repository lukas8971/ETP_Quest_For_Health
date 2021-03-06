import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {EquipmentService} from '../../service/equipment.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatTableDataSource} from '@angular/material/table';
import {Equipment} from '../../dto/equipment';
import {ErrorDialogComponent} from '../error-dialog/error-dialog.component';
import {Router} from '@angular/router';
import {User} from '../../dto/user';
import {HeaderInfoService} from '../../service/header-info.service';
import {UserService} from '../../service/user.service';
import {MessagesService} from '../../service/message-service';
import {faFistRaised} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-user-equipment',
  templateUrl: './user-equipment.component.html',
  styleUrls: ['./user-equipment.component.css']
})
export class UserEquipmentComponent implements OnInit {

  /**
   * Input to select the equipment type
   */
  @Input() type: string | undefined;

  /**
   * Output event if data changed
   */
  @Output()
  changedEvent: EventEmitter<string> = new EventEmitter<string>();

  userId = -1;
  equipment: any[] = [];
  equipped = false;
  available = false;
  equippedEquipment: any;
  selectedEquipment: any;
  equipmentColumns = ['name', 'strength', 'strength-diff'];
  dataSource = new MatTableDataSource(this.equipment);

  faStrength = faFistRaised;

  constructor(private dialog: MatDialog, private equipmentService: EquipmentService, private snackBar: MatSnackBar,
              private router: Router, private headerInfoService: HeaderInfoService, private userService: UserService,
              private messageService: MessagesService) {
    this.userId = Number(sessionStorage.getItem('userId'));
  }

  /**
   * Event called when the child has changed
   */
  public itemChanged(): void{
    console.log('Child changed');
    this.changedEvent.emit('Child changed!');
    this.reset();
    this.getEquippedItem();
  }

  ngOnInit(): void {
    this.getEquippedItem();
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
   * Redirects the user to the shop
   */
  public shopping(): void{
    console.log('Go shopping');
    this.router.navigate(['shop']);
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
   * Equips the selected item
   * @param e that should be worn
   */
  public equip(e: Equipment): void {
    console.log('equip');
    this.equippedEquipment = null;
    this.equipped = false;
    this.equipmentService.equipItem(this.userId, e).subscribe(
      (eq: Equipment) => {
        this.selectedEquipment = null;
        this.equipped = false;
        this.available = false;
        if (eq !== null && eq !== undefined) {
          this.equippedEquipment = eq;
          this.equipped = true;
          this.changedEvent.emit('Changed!');
          this.messageService.sendMessage({ message: 'equipment_changed', receiver: 'all'});
          console.log('Changed Message sent!');
        }
        this.getAvailableEquipment();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  public unequip(equippedEquipment: Equipment) {
    console.log('unequip');
    this.equipmentService.unequip(this.userId, equippedEquipment).subscribe(  success => {
      if(success) {
        this.selectedEquipment = null;
        this.equipped = false;
        this.available = true;
        this.equippedEquipment = null;
        this.changedEvent.emit('Changed!');
        this.messageService.sendMessage({ message: 'equipment_changed', receiver: 'all'});
        this.getAvailableEquipment();
      } else {
        const error = 'Could not unequip ' + equippedEquipment.name;
        this.defaultServiceErrorHandling(error);
      }
    }, error => {
      this.defaultServiceErrorHandling(error)
    })
  }

  /**
   * Gets the currently equipped item of the @type of the user
   */
  private getEquippedItem(): void{
    console.log('Get equipped item of ' + this.type);
    this.equipmentService.getEquipmentOfTypeWornByUserId(String(this.type), this.userId).subscribe(
      (eq: Equipment) => {
        if (eq !== null && eq !== undefined) {
          this.equippedEquipment = eq;
          this.equipped = true;
        }
        this.getAvailableEquipment();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Gets all the available equipments of type
   */
  private getAvailableEquipment(): void{
    console.log('Get available character equipment');
    this.equipmentService.getAvailableEquipmentToEquip(String(this.type), this.userId).subscribe(
      (eq: Equipment[]) => {

        if (eq !== null && eq !== undefined) {
          this.equipment = eq;
          console.log(eq);
          this.dataSource = new MatTableDataSource(this.equipment);
          this.available = true;
          this.selectedEquipment = eq[0];
          this.messageService.sendMessage({ message: 'equipment_changed', receiver: 'all'});
        }
        if(this.equippedEquipment !== null && this.equippedEquipment !== undefined) {
          if(this.available) {
            this.dataSource.data.push(this.equippedEquipment);
          } else {
            this.dataSource = new MatTableDataSource();
            this.dataSource.data.push(this.equippedEquipment);
            this.available = true;
          }

        }
        //this.getUserForHeader();
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Gets the current user for the header info component
   */
  /*
  private getUserForHeader(): void {
    console.log('getUserForHeader');
    this.userService.getUserById(this.userId).subscribe(
      (user: User) => {
        this.headerInfoService.setUser(user);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }*/

  /**
   * Resets the data
   */
  private reset(): void{
    this.selectedEquipment = null;
    this.equipped = false;
    this.available = false;
    this.equippedEquipment = null;
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
