import {Component, OnInit, ViewChild} from '@angular/core';
import {Quest} from "../../dto/quest";
import {MatTableDataSource} from "@angular/material/table";
import {QuestService} from "../../service/quest.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import * as moment from "moment";
import {Duration} from "moment";
import {MatSort} from '@angular/material/sort';
import {UserService} from "../../service/user.service";
import {User} from "../../dto/user";
import {UserQuest} from "../../dto/userQuest";

@Component({
  selector: 'app-user-overview',
  templateUrl: './user-overview.component.html',
  styleUrls: ['./user-overview.component.css']
})
export class UserOverviewComponent implements OnInit {
  repetitiveQuests: Quest[] = [];
  oneTimeQuests: Quest[] = [];
  questColumns: string[] = ['name', 'exp_reward','gold_reward','dueDate'];
  oneTimeQuestColumns: string[] = ['name', 'exp_reward','gold_reward'];
  repDataSource = new MatTableDataSource(this.repetitiveQuests);
  oneTimeDatSource = new MatTableDataSource(this.oneTimeQuests);
  selectedQuest: any;
  radioButtonQuests: string='Repetitive';
  repSort:MatSort = new MatSort();
  oneTimeSort: MatSort = new MatSort();
  currentDate: Date = new Date();
  user: any;

  @ViewChild(MatSort)  set matSort(ms: MatSort){
    this.repSort=ms;
    this.repDataSource.sort=this.repSort;
    this.oneTimeSort = ms;
    this.oneTimeDatSource.sort = this.oneTimeSort;
  }
  constructor(private questService:QuestService, private userService:UserService, private snackBar: MatSnackBar) { }


  ngOnInit(): void {
    this.loadRepetitiveQuests();
    this.loadOneTimeQuests();
    this.loadUser();
  }

  loadRepetitiveQuests(){
    this.questService.getAllQuestsDueForUser(Number(sessionStorage.getItem('userId'))).subscribe(
      (q: Quest[]) => {
        this.repetitiveQuests = q;
        this.repDataSource = new MatTableDataSource<Quest>(q);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )
  }

  loadUser(){
    this.userService.getUserById(Number(sessionStorage.getItem('userId'))).subscribe(
      (u:User) =>{
        this.user = u;
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )
  }

  loadOneTimeQuests(){
    this.questService.getAllOpenOneTimeQuestsForUser(Number(sessionStorage.getItem('userId'))).subscribe(
      (q: Quest[]) => {
        this.oneTimeQuests = q;
        this.oneTimeDatSource = new MatTableDataSource<Quest>(q);
        console.log(q);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )
  }

  public applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.repDataSource.filter = filterValue.trim().toLowerCase();
  }


  getDifference(date: string): number {
    let d1 = new Date(date);
    return this.millisecondsToDays(d1.getTime()-this.currentDate.getTime());
  }

  private millisecondsToDays(milli: number): number{
    return Math.floor((((milli/1000)/60)/60)/24);

  }

  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    this.snackBar.open('Could not load quests, please try again later', 'Ok');
  }
  public selectQuest(quest: Quest){
    this.selectedQuest =quest
  }

  public finishQuest(){
    let userQuest = () : UserQuest =>({
      quest: this.selectedQuest,
      user:this.user
    });
    this.userService.completeQuest(userQuest()).subscribe(
      (u:User) =>{
        if(u.characterLevel !== this.user.characterLevel){
          this.snackBar.open('You leveled up!', 'Great!');
        }
        this.snackBar.open('YOSSSS', 'OK');
        this.user = u;
        this.repDataSource.data = this.repDataSource.data.filter(item => item !== this.selectedQuest);
        this.oneTimeDatSource.data = this.oneTimeDatSource.data.filter(item => item !== this.selectedQuest);
        this.selectedQuest=null;
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )
  }

}
