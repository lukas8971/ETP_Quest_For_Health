import { Component, OnInit } from '@angular/core';
import {AuthenticationDoctorService} from '../../service/authentication-doctor.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public doctorLoginService: AuthenticationDoctorService) { }

  ngOnInit(): void {
  }

}
