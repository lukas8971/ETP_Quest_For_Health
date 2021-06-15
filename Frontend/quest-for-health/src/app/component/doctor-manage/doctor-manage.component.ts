import { Component, OnInit } from '@angular/core';
import {ErrorDialogComponent} from '../error-dialog/error-dialog.component';
import {DoctorUserRelation} from '../../dto/doctor-user-relation';
import {DoctorService} from '../../service/doctor.service';
import {MatDialog} from '@angular/material/dialog';
import {MatTableDataSource} from '@angular/material/table';
import {Quest} from '../../dto/quest';
import {User} from '../../dto/user';
import {UserService} from '../../service/user.service';

@Component({
  selector: 'app-doctor-manage',
  templateUrl: './doctor-manage.component.html',
  styleUrls: ['./doctor-manage.component.css']
})
export class DoctorManageComponent implements OnInit {

  patHeader = ['firstname', 'lastname', 'id'];
  docId = 0;
  treatPats: any[] = [];
  availablePats: any[] = [];
  dataSourceTreat = new MatTableDataSource(this.treatPats);
  dataSourceAvailable = new MatTableDataSource(this.availablePats);
  showTreat = false;
  showAvailable = false;

  constructor(private doctorService: DoctorService, private dialog: MatDialog, private userService: UserService) { }

  ngOnInit(): void {
    this.docId = Number(sessionStorage.getItem('id'));
    this.loadAvailable();
    this.loadTreat();
  }

  /**
   * Gets the available patients from a doctor
   */
  private loadAvailable(): void {
    console.log('Get available patients for a doctor');
    this.userService.getAllNotUsersFroMDoctor(this.docId).subscribe(
      (u: User[]) => {
        this.availablePats = u;
        this.dataSourceAvailable = new MatTableDataSource(this.availablePats);
        this.showAvailable = true;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Get all the Patients from the doctor
   */
  public loadTreat(): void{
    console.log('Get all patients of doctor');
    this.userService.getAllUsersFromDoctor(this.docId).subscribe(
      (u: User[]) => {
        this.treatPats = u;
        this.dataSourceTreat = new MatTableDataSource(u);
        this.showTreat = true;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Filter for the patients in treatment
   * @param event the given filter
   */
  public applyFilterTreat(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSourceTreat.filter = filterValue.trim().toLowerCase();
  }

  /**
   * Filter for the available patients
   * @param event the given filter
   */
  public applyFilterAvailable(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSourceAvailable.filter = filterValue.trim().toLowerCase();
  }

  /**
   * Adds the patient to the doctor
   * @param id of the patient that should be assigned to the doctor
   */
  public addPat(id: number): void{
    console.log('Add patient ' + id + ' to doctor ' + this.docId);
    const docRel = new DoctorUserRelation(this.docId, id);
    this.doctorService.assignNewPatient(docRel).subscribe(
      (dr: DoctorUserRelation) => {
        this.resetValues();
        this.loadAvailable();
        this.loadTreat();
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Removes the patient from the doctor
   * @param id of the patient that should be removed from the doctor
   */
  public removePat(id: number): void{
    console.log('Add patient ' + id + ' to doctor ' + this.docId);
    const docRel = new DoctorUserRelation(this.docId, id);
    this.doctorService.removePatient(docRel).subscribe(
      (b: boolean) => {
        if (b) {
          this.resetValues();
          this.loadAvailable();
          this.loadTreat();
        } else {
          this.dialog.open(ErrorDialogComponent, {
            data: { err: 'Could not remove the user!', message: 'Could not remove the user!' }
          });
        }
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Resets all values for the UI
   */
  private resetValues(): void{
    this.availablePats = [];
    this.dataSourceAvailable = new MatTableDataSource(this.availablePats);
    this.showAvailable = false;
    this.treatPats = [];
    this.dataSourceTreat = new MatTableDataSource(this.treatPats);
    this.showTreat = false;
  }

  /**
   * Showsan error message if an error occurs
   * @param error that should be displayed
   */
  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    this.dialog.open(ErrorDialogComponent, {
      data: { err: error, message: '' }
    });
  }
}
