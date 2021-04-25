import { Component, OnInit } from '@angular/core';
import {Quest} from "../../dto/quest";
import {QuestService} from "../../service/quest.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatTableDataSource} from "@angular/material/table";

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


  constructor(private questService: QuestService, private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.loadQuests();
  }

  loadQuests() {
    this.questService.getNewQuestsForUser(Number(sessionStorage.getItem('userId'))).subscribe(
      (q: Quest[]) => {
        this.quests = q;
        this.dataSource = new MatTableDataSource<Quest>(q);
        console.log(q);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    )

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
