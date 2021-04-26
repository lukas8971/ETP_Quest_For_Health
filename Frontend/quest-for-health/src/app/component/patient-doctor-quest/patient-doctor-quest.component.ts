import { Component, OnInit } from '@angular/core';
import {ErrorDialogComponent} from '../error-dialog/error-dialog.component';
import {DoctorService} from '../../service/doctor.service';
import {UserService} from '../../service/user.service';
import {MatDialog} from '@angular/material/dialog';
import {ActivatedRoute, Router} from '@angular/router';
import {Doctor} from '../../dto/doctor';
import {User} from '../../dto/user';
import {QuestService} from '../../service/quest.service';
import {Quest} from '../../dto/quest';
import {AcceptedQuest} from '../../dto/accepted-quest';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-patient-doctor-quest',
  templateUrl: './patient-doctor-quest.component.html',
  styleUrls: ['./patient-doctor-quest.component.css']
})
export class PatientDoctorQuestComponent implements OnInit {

  patient: any;
  patientReady = false;
  doctor: any;
  assignedQuests: any[] = [];
  availableQuests: any[] = [];
  showAvailable = false;
  showAssigned = false;

  dataSourceAssigned = new MatTableDataSource(this.assignedQuests);
  dataSourceAvailable = new MatTableDataSource(this.availableQuests);

  questHeader = ['name', 'description', 'id'];

  constructor(private doctorService: DoctorService, private userService: UserService,
              private dialog: MatDialog, private questService: QuestService, private route: ActivatedRoute, private router: Router) {
    this.route.queryParams.subscribe(queryParams => {
      if (queryParams.user !== undefined){
        console.log('queryParams: ' + queryParams.user);
        if (!isNaN(Number(sessionStorage.getItem('id'))) && !isNaN(Number(queryParams.user))){
          this.getUserById(Number(queryParams.user), Number(sessionStorage.getItem('id')));
          this.getAssignedPatientDoctorQuests(Number(sessionStorage.getItem('id')), Number(queryParams.user));
          this.getAvailablePatientDoctorQuests(Number(sessionStorage.getItem('id')), Number(queryParams.user));
        }
      }
    });
  }

  ngOnInit(): void {
  }

  /**
   * Filter for the assigned quests
   * @param event the given filter
   */
  public applyFilterAssign(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSourceAssigned.filter = filterValue.trim().toLowerCase();
  }

  /**
   * Filter for the available quests
   * @param event the given filter
   */
  public applyFilterAvailable(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSourceAvailable.filter = filterValue.trim().toLowerCase();
  }

  /**
   * Remove a doctor assigned quest from a patient
   * @param id of the quest that should be removed from the patient
   */
  public removeQuest(id: number): void{
    console.log('Remove quest ' + id + ' from user ' + this.patient.firstname + ' ' + this.patient.lastname);
    this.questService.deleteAssignedDoctorQuestForUser(id, this.patient.id).subscribe(
      (b: boolean) => {
        if (b){
          this.resetValues();
          this.getAvailablePatientDoctorQuests(this.doctor.id, this.patient.id);
          this.getAssignedPatientDoctorQuests(this.doctor.id, this.patient.id);
        }
        else{
          this.dialog.open(ErrorDialogComponent, {
            data: { err: 'Could not remove the quest from the user!', message: 'Could not add the quest to the user!' }
          });
        }
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Add a doctor assigned quest to a patient
   * @param id of the quest that should be assigned to a patient
   */
  public addQuest(id: number): void{
    console.log('Add quest ' + id + ' to user ' + this.patient.firstname + ' ' + this.patient.lastname);
    const accQuest = new AcceptedQuest(id, this.patient.id, new Date());
    this.questService.addAssignedDoctorQuestForUser(accQuest).subscribe(
      (b: boolean) => {
        if (b){
          this.resetValues();
          this.getAvailablePatientDoctorQuests(this.doctor.id, this.patient.id);
          this.getAssignedPatientDoctorQuests(this.doctor.id, this.patient.id);
        }
        else{
          this.dialog.open(ErrorDialogComponent, {
            data: { err: 'Could not add the quest to the user!', message: 'Could not add the quest to the user!' }
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
    this.availableQuests = [];
    this.dataSourceAvailable = new MatTableDataSource(this.availableQuests);
    this.showAvailable = false;
    this.assignedQuests = [];
    this.dataSourceAssigned = new MatTableDataSource(this.assignedQuests);
    this.showAssigned = false;
  }

  /**
   * Get the logged in user information
   * @param user patient of the doctor
   */
  private getUserById(user: number, doctor?: number): void{
    console.log('Get doctor information');
    this.userService.getUserById(user).subscribe(
      (u: User) => {
        this.patient = u;
        this.patientReady = true;
        if (doctor !== null && doctor !== undefined){
          this.getDoctorById(doctor);
        }
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
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
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Gets the available quests from a doctor for a user
   * @param doctor of the patient
   * @param patient to assign the quests for
   */
  private getAvailablePatientDoctorQuests(doctor: number, patient: number): void {
    console.log('Get available patient doctor quests');
    this.questService.getAvailableDoctorQuestsForUser(doctor, patient).subscribe(
      (q: Quest[]) => {
        this.availableQuests = q;
        this.dataSourceAvailable = new MatTableDataSource(this.availableQuests);
        this.showAvailable = true;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Gets the assigned quests from a doctor for a user
   * @param doctor of the patient
   * @param patient to assign the quests for
   */
  private getAssignedPatientDoctorQuests(doctor: number, patient: number): void {
    console.log('Get assigned patient doctor quests');
    this.questService.getAssignedDoctorQuestsForUser(doctor, patient).subscribe(
      (q: Quest[]) => {
        this.assignedQuests = q;
        this.dataSourceAssigned = new MatTableDataSource(this.assignedQuests);
        this.showAssigned = true;
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
  }

  createDoctorQuest() {
    console.log("createDoctorQuest();");
    this.router.navigate(['doctors/createquest'], {replaceUrl: true});
  }
}
