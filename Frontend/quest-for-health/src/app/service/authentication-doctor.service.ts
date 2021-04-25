import { Injectable } from '@angular/core';
import {Credentials} from '../dto/credentials';
import {DoctorService} from './doctor.service';
import {Doctor} from '../dto/doctor';
import {ErrorDialogComponent} from '../component/error-dialog/error-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {ErrorData} from '../entity/error-data';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationDoctorService {

  err: ErrorData | undefined;

  constructor(private doctorService: DoctorService, private dialog: MatDialog, private router: Router) { }

  authenticate(cred: Credentials): void {
    if (cred !== null && cred !== undefined) {
      this.doctorService.checkLogin(cred).subscribe(
        (d: Doctor) => {
          sessionStorage.setItem('id', String(d.id));
          this.router.navigate(['/doctors/overview']);
        },
        error => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      this.defaultServiceErrorHandling('Missing value for email or password!');
    }
  }

  /**
   * Shows an error message if an error occurs
   * @param error that should be displayed
   */
  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    if (typeof error === 'string'){
      this.dialog.open(ErrorDialogComponent, {
        data: { err: null, message: error }
      });
    }
    else {
      this.dialog.open(ErrorDialogComponent, {
        data: { err: error, message: '' }
      });
    }

  }

  /**
   * Checks if a doctor is still logged in
   */
  isUserLoggedIn(): boolean {
    const doctor = sessionStorage.getItem('id');
    return !(doctor === null);
  }

  /**
   * Logs out a doctor
   */
  logOut(): void {
    sessionStorage.removeItem('id');
  }
}
