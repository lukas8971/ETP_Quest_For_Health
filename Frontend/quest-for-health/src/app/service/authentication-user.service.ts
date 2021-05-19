import { Injectable } from '@angular/core';
import {Credentials} from '../dto/credentials';
import {UserService} from './user.service';
import {User} from '../dto/user';
import {ErrorDialogComponent} from '../component/error-dialog/error-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {ErrorData} from '../entity/error-data';
import {HeaderInfoService} from './header-info.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationUserService {

  err: ErrorData | undefined;

  constructor(private userService: UserService, private dialog: MatDialog, private router: Router,
              private headerInfoService: HeaderInfoService) { }

  authenticate(cred: Credentials): void {
    if (cred !== null && cred !== undefined) {
      this.userService.checkLogin(cred).subscribe(
        (u: User) => {
          sessionStorage.setItem('userId', String(u.id));
          this.router.navigate(['/users/overview']);
          this.headerInfoService.setUser(u);
        },
        error => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      this.defaultServiceErrorHandling('Missing value for character-name or password!');
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
   * Checks if a user is still logged in
   */
  isUserLoggedIn(): boolean {
    const user = sessionStorage.getItem('userId');
    return !(user === null);
  }

  /**
   * Logs out a user
   */
  logOut(): void {
    sessionStorage.removeItem('userId');
  }
}
