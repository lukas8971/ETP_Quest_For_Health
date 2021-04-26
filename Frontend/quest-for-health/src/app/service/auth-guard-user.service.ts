import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthenticationUserService} from "./authentication-user.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardUserService implements CanActivate{

  constructor(private router: Router, private authService: AuthenticationUserService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.authService.isUserLoggedIn()) {
      return true;
    }
    this.router.navigate(['userLogin']);
    return false;
  }
}
