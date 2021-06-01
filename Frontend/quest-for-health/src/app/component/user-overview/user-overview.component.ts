import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Quest} from "../../dto/quest";
import {MatTableDataSource} from "@angular/material/table";
import {QuestService} from "../../service/quest.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatSort} from '@angular/material/sort';
import {UserService} from "../../service/user.service";
import {User} from "../../dto/user";
import {UserQuest} from "../../dto/userQuest";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {UserQuests} from "../../dto/userQuests";
import {MatInputModule} from "@angular/material/input";
import { MatFormFieldModule } from "@angular/material/form-field";
import {HeaderInfoService} from '../../service/header-info.service';
import {faCoins, faDiceD20, faFistRaised, faScroll} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-user-overview',
  templateUrl: './user-overview.component.html',
  styleUrls: ['./user-overview.component.css']
})
export class UserOverviewComponent implements OnInit {
  repetitiveQuests: Quest[] = [];
  questsThisWeek: Quest[] = [];
  oneTimeQuests: Quest[] = [];
  questColumns: string[] = ['name','dueDate'];
  oneTimeQuestColumns: string[] = ['name', 'exp_reward','gold_reward'];
  repDataSource = new MatTableDataSource(this.repetitiveQuests);
  weeklyRepDataSource = new MatTableDataSource(this.questsThisWeek);
  oneTimeDatSource = new MatTableDataSource(this.oneTimeQuests);
  selectedQuest: any;
  radioButtonQuests: string='Repetitive';
  repSort:MatSort = new MatSort();
  weekSort:MatSort = new MatSort();
  oneTimeSort: MatSort = new MatSort();
  currentDate: Date = new Date();
  user: any;

  faCoins = faCoins;
  faStrength = faFistRaised;
  faExp = faDiceD20;
  faStory = faScroll;

  @ViewChild(MatSort)  set matSort(ms: MatSort){
    this.repSort=ms;
    this.repDataSource.sort=this.repSort;
    this.oneTimeSort = ms;
    this.oneTimeDatSource.sort = this.oneTimeSort;
    this.weekSort = ms;
    this.weeklyRepDataSource.sort = this.weekSort;
  }
  constructor(private questService:QuestService, private userService:UserService, private snackBar: MatSnackBar, private dialog: MatDialog,
              private headerInfoService: HeaderInfoService) { }


  ngOnInit(): void {
    this.currentDate.setHours(0,0,0,0,);
    this.loadQuests()
    this.loadUser();
  }

  loadRepetitiveQuests(){
    this.questService.getAllQuestsDueForUser(Number(sessionStorage.getItem('userId'))).subscribe(
      (q: Quest[]) => {
        const nextWeekDate = new Date();
        nextWeekDate.setDate(nextWeekDate.getDate() + 7);
        this.repetitiveQuests = q.filter(x => new Date(x.dueDate).getTime() >= nextWeekDate.getTime()).sort((a:Quest, b:Quest) =>{
          return new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime();
        });
        this.questsThisWeek = q.filter(x => new Date(x.dueDate).getTime() <= nextWeekDate.getTime()).sort((a:Quest, b: Quest) =>{
          return new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime();
        });
        this.repDataSource = new MatTableDataSource<Quest>(this.repetitiveQuests);
        this.weeklyRepDataSource = new MatTableDataSource<Quest>(this.questsThisWeek);


      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )
  }
  loadQuests(){
    this.questService.getAllMissedQuestsForUser(Number(sessionStorage.getItem('userId'))).subscribe(
      (q: Quest[]) => {
        if(q.length === 0){
          this.loadRepetitiveQuests();
          this.loadOneTimeQuests();
        } else{
          this.openDialog(q);
        }
      }
      // open first quest
    )
  }

  openDialog(quests: Quest[]):void {
    const dialogRef = this.dialog.open(MissedQuestsDialog, {
      width: '50%',
      data: {quests: quests }
    });
    dialogRef.afterClosed().subscribe(result =>{
      let userQuests = () : UserQuests =>({
        quests: quests,
        user:this.user
      })
      this.userService.dismissMissedQuests(userQuests()).subscribe(
        (u:User) => {
          this.user = u;
          this.loadRepetitiveQuests();
          this.loadOneTimeQuests();
          this.headerInfoService.setUser(this.user);
        }
      )

    })
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
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )
  }

  public applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.repDataSource.filter = filterValue.trim().toLowerCase();
    this.oneTimeDatSource.filter = filterValue.trim().toLowerCase();
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
  public selectQuest(quest: Quest): void {
    this.selectedQuest = quest;
  }

  public finishQuest(){
    let userQuest = () : UserQuest =>({
      quest: this.selectedQuest,
      user:this.user
    });
    this.userService.completeQuest(userQuest()).subscribe(
      (u:User) =>{
        this.snackBar.open('Quest completed!', 'OK');
        if(u.characterLevel !== this.user.characterLevel){
          this.snackBar.open('You leveled up!', 'Great!');
          this.checkUserForNextStoryAndUpdate();
        }this.user = u;
        this.weeklyRepDataSource.data = this.weeklyRepDataSource.data.filter(item => item !== this.selectedQuest);
        this.repDataSource.data = this.repDataSource.data.filter(item => item !== this.selectedQuest);
        this.oneTimeDatSource.data = this.oneTimeDatSource.data.filter(item => item !== this.selectedQuest);
        this.selectedQuest=null;
        this.headerInfoService.setUser(this.user);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )
  }

  /**
   * Checks the chapter of a user and updates it if the strength is high enough
   */
  private checkUserForNextStoryAndUpdate(): void {
    console.log('checkUserForNextStoryAndUpdate');
    this.userService.checkUserForNextStoryAndUpdate(this.user.id).subscribe(
      (up: boolean) => {
        if (up) {
          this.snackBar.open('Great. You got to the next chapter!', 'Yasss');
        }
        this.headerInfoService.setUser(this.user);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

}

export interface DialogData {
  quests: Quest[];
}

@Component(
  {
    selector:'missed-quest-dialog',
    templateUrl: 'missed-quest-dialog.html',
  })
export class MissedQuestsDialog{
  constructor(private dialogRef: MatDialogRef<MissedQuestsDialog>,@Inject(MAT_DIALOG_DATA) public data: DialogData ) {
    }
  close(): void{
    this.dialogRef.close();

  }
  getTotalGoldPenalty(): number {
    let goldPenalty = 0;
    this.data.quests.forEach(function (quest) {
      goldPenalty += quest.gold_penalty;
    })
    return goldPenalty;
  }
  getTotalExpPenalty(): number {
    let expPenalty = 0;
    this.data.quests.forEach(function (quest) {
      expPenalty += quest.exp_penalty;
    })
    return expPenalty;
  }
}
