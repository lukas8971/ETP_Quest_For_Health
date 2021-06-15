import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {Credentials} from "../../dto/credentials";
import {AuthenticationUserService} from "../../service/authentication-user.service";

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  fg = new FormGroup({
    characterName: new FormControl(),
    password: new FormControl()
  });

  characterName = '';
  password = '';

  constructor(private router: Router, private loginUser: AuthenticationUserService) {
  }

  public ngOnInit(): void {
  }

  /**
   * Login of a doctor
   */
  checkLogin(): void {
    this.loginUser.authenticate(new Credentials(this.characterName, this.password));
  }
}
