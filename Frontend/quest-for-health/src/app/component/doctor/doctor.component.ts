import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationDoctorService} from '../../service/authentication-doctor.service';
import {Credentials} from '../../dto/credentials';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css']
})
export class DoctorComponent implements OnInit {

  fg = new FormGroup({
    email: new FormControl(),
    password: new FormControl()
  });

  email = '';
  password = '';

  constructor(private router: Router, private loginDoctor: AuthenticationDoctorService) {
  }

  public ngOnInit(): void {
  }

  /**
   * Login of a doctor
   */
  checkLogin(): void {
    this.loginDoctor.authenticate(new Credentials(this.email, this.password));
  }
}
