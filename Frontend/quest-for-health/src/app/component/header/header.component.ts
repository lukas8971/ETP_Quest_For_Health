import { Component, OnInit } from '@angular/core';
import {AuthenticationDoctorService} from '../../service/authentication-doctor.service';
import {AuthenticationUserService} from '../../service/authentication-user.service';
import {User} from '../../dto/user';
import {HeaderInfoService} from '../../service/header-info.service';
import {faTrophy} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public doctorLoginService: AuthenticationDoctorService, public userLoginService: AuthenticationUserService,
              private headerInfoService: HeaderInfoService) { }

  faTrophy = faTrophy;

 user: User = {id: -1, firstname: '', lastname: '', characterName: '',
   characterStrength: -1, characterLevel: -1, password: '', email: '', storyChapter: -1, characterExp: -1, characterGold: -1};

  ngOnInit(): void {
    this.headerInfoService.user.subscribe(updatedUser => {
      this.user = updatedUser;
    });
  }

  logOut(): void{
    this.doctorLoginService.logOut();
    this.userLoginService.logOut();
  }

}
