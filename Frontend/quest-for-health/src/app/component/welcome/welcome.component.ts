import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationDoctorService} from '../../service/authentication-doctor.service';
import {AuthenticationUserService} from '../../service/authentication-user.service';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  constructor(private router: Router, private doctorLoginService: AuthenticationDoctorService,
              private userLoginService: AuthenticationUserService) { }

  ngOnInit(): void {
    if (this.doctorLoginService.isUserLoggedIn()) {
      this.router.navigate(['doctors/overview']);
    }
    if (this.userLoginService.isUserLoggedIn()) {
      this.router.navigate(['users/profile']);
    }
  }
}
