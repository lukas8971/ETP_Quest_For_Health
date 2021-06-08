import {Component, OnInit} from '@angular/core';
import {AuthenticationDoctorService} from "./service/authentication-doctor.service";
import {AuthenticationUserService} from "./service/authentication-user.service";
import {faTrophy} from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements  OnInit{
  title = 'quest-for-health';
  faTrophy = faTrophy;


  constructor(public doctorLoginService: AuthenticationDoctorService, public userLoginService: AuthenticationUserService) {
  }

  ngOnInit(): void {
  }
}
