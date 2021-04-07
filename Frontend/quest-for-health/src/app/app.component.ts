import {Component, OnInit} from '@angular/core';
import {User} from "./dto/user";
import {UserService} from "./service/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements  OnInit{
  title = 'quest-for-health';
  users: User[] | undefined;

  constructor(private userService:UserService) {
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  private loadUsers(){
    this.userService.getAllUsers().subscribe(
      (users: User[]) =>{
        this.users = users;
      }, error =>{
        return;
      }
    );
  }
}
