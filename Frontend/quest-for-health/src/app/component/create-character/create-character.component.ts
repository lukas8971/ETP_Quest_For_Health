import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, FormBuilder, Validators} from '@angular/forms';
import {UserService} from '../../service/user.service';
import {ErrorDialogComponent} from '../error-dialog/error-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material/snack-bar';
import {User} from '../../dto/user';
import {Router} from '@angular/router';


@Component({
  selector: 'app-create-character',
  templateUrl: './create-character.component.html',
  styleUrls: ['./create-character.component.css']
})
export class CreateCharacterComponent implements OnInit {
  form: FormGroup;
  passwordRepeat = '';
  user: User = {
    firstname: '',
    lastname: '',
    characterName: '',
    email: '',
    password: '',
    id: 0,
    characterStrength: 0,
    characterLevel: 0,
    storyChapter: 0,
    characterExp: 0,
    characterGold: 0
  };

  constructor(private snackBar: MatSnackBar, private formBuilder: FormBuilder, private userService: UserService,
              private dialog: MatDialog, private router: Router) {
     this.form = this.formBuilder.group({
       firstname: [this.user.firstname, Validators.required],
       lastname: [this.user.lastname, Validators.required],
       characterName: [this.user.characterName, Validators.required],
       email: [this.user.email, Validators.email],
       password: [this.user.password, Validators.required],
       passwordRepeat: ['', Validators.required]
       });
   }

  ngOnInit(): void {
  }

  createCharacter(): void{
  if (this.form.valid){
    this.userService.createUser(this.user).subscribe(
      () => {
        this.snackBar.open('User created successfully!', 'Great');
        this.router.navigate(['/']);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    ); } else{
    }
  }

  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    if (typeof error === 'string'){
      this.dialog.open(ErrorDialogComponent, {
        data: { err: null, message: error }
      });
    }
    else {
      this.dialog.open(ErrorDialogComponent, {
        data: { err: error, message: '' }
      });
    }
  }

}
