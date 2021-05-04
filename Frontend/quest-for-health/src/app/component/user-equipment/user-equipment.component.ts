import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {EquipmentService} from '../../service/equipment.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatTableDataSource} from '@angular/material/table';
import {Equipment} from '../../dto/equipment';
import {ErrorDialogComponent} from '../error-dialog/error-dialog.component';
import {Router} from '@angular/router';

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

  constructor(private dialog: MatDialog, private equipmentService: EquipmentService, private snackBar: MatSnackBar,
              private router: Router) {
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
  public equip(e: Equipment): void{
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
        }
        this.getAvailableEquipment();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
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
        }
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

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
