import { Component, OnInit } from '@angular/core';
import {Quest} from "../../dto/quest";
import {QuestService} from "../../service/quest.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatTableDataSource} from "@angular/material/table";
import {faCoins, faDiceD20, faFistRaised, faScroll} from '@fortawesome/free-solid-svg-icons';
import * as moment from "moment";
import {Duration} from "moment";


@Component({
  selector: 'app-user-browse-quests',
  templateUrl: './user-browse-quests.component.html',
  styleUrls: ['./user-browse-quests.component.css']
})
export class UserBrowseQuestsComponent implements OnInit {
  quests: Quest[] = [];
  selectedQuest: any;
  dataSource = new MatTableDataSource(this.quests);
  questColumns: string[] = ['name', 'exp_reward','gold_reward','repetition_cycle'];
  myMoment: any;

  faCoins = faCoins;
  faStrength = faFistRaised;
  faExp = faDiceD20;
  faStory = faScroll;

  constructor(private questService: QuestService, private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.myMoment = moment;
    this.loadQuests();
  }

  loadQuests() {
    this.questService.getNewQuestsForUser(Number(sessionStorage.getItem('userId'))).subscribe(
      (q: Quest[]) => {
        this.quests = q;
        this.dataSource = new MatTableDataSource<Quest>(q);
        this.selectedQuest = q[0];
        console.log(q);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )

  }

  momentToDays(days: string): string {
   let d_cycle: Duration = moment.duration(days);
    return d_cycle.asDays().toString();
  }

  public selectQuest(quest: Quest){
    this.selectedQuest =quest
  }

  public acceptQuest(){
    this.questService.acceptQuest(Number(sessionStorage.getItem('userId')), this.selectedQuest.id).subscribe(
      (b: boolean) =>{
        this.dataSource.data = this.dataSource.data.filter(item => item !== this.selectedQuest);
        this.snackBar.open('Quest has been successfully added to your open Quests', 'OK');
        this.selectedQuest = undefined;
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )

  }

  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    this.snackBar.open('Could not load quests, please try again later', 'Ok');
  }

  /**
   * Filter for the quest table
   * @param event the given filter
   */
  public applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
