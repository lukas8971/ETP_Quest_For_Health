import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {EquipmentService} from '../../service/equipment.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Equipment} from '../../dto/equipment';
import {ErrorDialogComponent} from '../error-dialog/error-dialog.component';

@Component({
  selector: 'app-equipped-item',
  templateUrl: './equipped-item.component.html',
  styleUrls: ['./equipped-item.component.css']
})
export class EquippedItemComponent implements OnInit {

  /**
   * Input to select the equipment type
   */
  @Input() equipmentId: string | undefined;

  /**
   * Output event if data changed
   */
  @Output()
  changedEvent: EventEmitter<string> = new EventEmitter<string>();

  userId = -1;
  equipment: any[] = [];
  equipped: any;
  worn = false;
  equipmentColumns = ['name', 'strength'];

  constructor(private dialog: MatDialog, private equipmentService: EquipmentService, private snackBar: MatSnackBar) {
    this.userId = Number(sessionStorage.getItem('userId'));
  }

  ngOnInit(): void {
    if (this.equipmentId !== undefined && Number(this.equipmentId) > 0){
      this.getWornEquipment();
    } else {
      this.worn = false;
      this.equipment = [];
      this.equipped = null;
    }
  }

  /**
   * Unequips this item
   */
  public unequip(): void{
    console.log('unequip');
    if (this.equipped !== null && this.equipped !== undefined){
      this.equipmentService.unequip(Number(this.userId), this.equipped).subscribe(
        (ret: boolean) => {
          if (ret){
            this.worn = false;
            this.equipment = [];
            this.equipped = null;
            this.snackBar.open('Unequipped item!', 'Great');
            this.changedEvent.emit('Changed!');
          } else {
            this.snackBar.open('Could not unequip item!', 'OK');
          }
        }, error => {
          this.defaultServiceErrorHandling(error);
        }
      );
    }
  }

  /**
   * Gets the currently worn equipment
   */
  private getWornEquipment(): void{
    console.log('Get the worn equipment');
    this.equipmentService.getOneById(Number(this.equipmentId)).subscribe(
      (eq: Equipment) => {
        this.equipment.push(eq);
        this.equipped = eq;
        this.worn = true;
      }, error => {
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
