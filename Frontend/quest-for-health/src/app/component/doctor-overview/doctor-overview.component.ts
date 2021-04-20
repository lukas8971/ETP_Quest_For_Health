import { Component, OnInit } from '@angular/core';
import {DoctorService} from '../../service/doctor.service';
import {Doctor} from '../../dto/doctor';
import {UserService} from '../../service/user.service';
import {User} from '../../dto/user';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import {ErrorDialogComponent} from '../error-dialog/error-dialog.component';
import {Router} from '@angular/router';

@Component({
  selector: 'app-doctor-overview',
  templateUrl: './doctor-overview.component.html',
  styleUrls: ['./doctor-overview.component.css']
})
export class DoctorOverviewComponent implements OnInit {

  constructor(private doctorService: DoctorService, private userService: UserService,
              private dialog: MatDialog, private router: Router) { }

  success = false;
  patients: any[] = [];
  doctorLoaded = false;
  doctor: any;

  ngOnInit(): void {
    this.getDoctorById(2);
  }

  /**
   * Resets the "successful-box"
   */
  public resetMessage(): void {
    console.log('Reset successful message');
    this.success = false;
  }

  /**
   * Get all the Patients from the doctor
   */
  public getAllPatients(doctor: number): void{
    console.log('Get all patients of doctor');
    this.userService.getAllUsersFromDoctor(doctor).subscribe(
      (u: User[]) => {
        this.patients = u;
        this.success = true;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Redirects to patient-doctor-quest
   * @param patient id for the quests
   */
  public showPatientQuests(patient: number): void {
    console.log('Get patient doctor quests');
    this.router.navigate(['doctors/patientquest'], {queryParams: {doctor: this.doctor.id, user: patient}, replaceUrl: true});
  }

  /**
   * Get the logged in doctor information
   * @param doctor that is currently logged in
   */
  private getDoctorById(doctor: number): void{
    console.log('Get doctor information');
    this.doctorService.getDoctorById(doctor).subscribe(
      (d: Doctor) => {
        this.doctor = d;
        this.doctorLoaded = true;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
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
    /*
    Wenn asynchron:
    const dialogRef = this.dialog.open(ErrorDialogComponent, {
      data: { err: error, message: '' }
    });
    dialogRef.afterClosed().subscribe(result => {
      something();
    });
     */
  }
}
