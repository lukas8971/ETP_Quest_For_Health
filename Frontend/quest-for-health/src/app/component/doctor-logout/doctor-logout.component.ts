import { Component, OnInit } from '@angular/core';
import {AuthenticationDoctorService} from '../../service/authentication-doctor.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-doctor-logout',
  templateUrl: './doctor-logout.component.html',
  styleUrls: ['./doctor-logout.component.css']
})
export class DoctorLogoutComponent implements OnInit {

  constructor(private router: Router, private authenticationDoctor: AuthenticationDoctorService) { }

  ngOnInit(): void {
    this.authenticationDoctor.logOut();
    this.router.navigate(['/doctors']);
  }

}
