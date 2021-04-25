import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthenticationDoctorService} from './authentication-doctor.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardDoctorService implements CanActivate{

  constructor(private router: Router, private authService: AuthenticationDoctorService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.authService.isUserLoggedIn()) {
      return true;
    }
    this.router.navigate(['doctors']);
    return false;
  }
}
