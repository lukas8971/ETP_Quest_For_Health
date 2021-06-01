import { Component, OnInit } from '@angular/core';
import {AuthenticationDoctorService} from '../../service/authentication-doctor.service';
import {AuthenticationUserService} from '../../service/authentication-user.service';
import {User} from '../../dto/user';
import {HeaderInfoService} from '../../service/header-info.service';
import {faHeartbeat, faScroll, faTrophy} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public doctorLoginService: AuthenticationDoctorService, public userLoginService: AuthenticationUserService,
              private headerInfoService: HeaderInfoService, private userService: UserService) { }

  faTrophy = faTrophy;
  faStory = faScroll;
  faHeart = faHeartbeat;

 user: User = {id: -1, firstname: '', lastname: '', characterName: '',
   characterStrength: -1, characterLevel: -1, password: '', email: '', storyChapter: -1, characterExp: -1, characterGold: -1};

  ngOnInit(): void {
    this.loadUser();
    this.headerInfoService.user.subscribe(updatedUser => {
      this.user = updatedUser;
    });
  }

  logOut(): void{
    this.doctorLoginService.logOut();
    this.userLoginService.logOut();
  }

  private loadUser() {
    this.userService.getUserById(Number(sessionStorage.getItem('userId'))).subscribe(
      (u: User) => {
        this.user = u;
      }, error => {}
    );
  }

}
