import {Component, OnInit} from '@angular/core';
import {AuthenticationDoctorService} from "./service/authentication-doctor.service";
import {AuthenticationUserService} from "./service/authentication-user.service";
import {faHeartbeat, faScroll, faTrophy, faLocationArrow, faUserAlt, faStore, faBook} from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements  OnInit{
  title = 'quest-for-health';
  faTrophy = faTrophy;
  faStory = faScroll;
  faHeart = faHeartbeat;
  faLocationArrow = faLocationArrow;
  faUser = faUserAlt;
  faStore = faStore;
  faBook = faBook;

  constructor(public doctorLoginService: AuthenticationDoctorService, public userLoginService: AuthenticationUserService) {
  }

  ngOnInit(): void {
  }
}
