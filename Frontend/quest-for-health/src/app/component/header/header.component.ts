import { Component, OnInit } from '@angular/core';
import {AuthenticationDoctorService} from '../../service/authentication-doctor.service';
import {AuthenticationUserService} from "../../service/authentication-user.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public doctorLoginService: AuthenticationDoctorService, public userLoginService: AuthenticationUserService) { }

  ngOnInit(): void {
  }
  logOut(){
    this.doctorLoginService.logOut();
    this.userLoginService.logOut();
  }

}
