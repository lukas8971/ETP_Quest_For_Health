import {Component, OnInit, Query} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Credentials} from "../../dto/credentials";
import {Quest} from "../../dto/quest";
import {QuestService} from "../../service/quest.service";
import {CreateDoctorQuest} from "../../dto/createDoctorQuest";
import {ErrorDialogComponent} from "../error-dialog/error-dialog.component";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import * as moment from "moment";
import {Duration} from "moment";
import { Location } from '@angular/common';
import {AutosizeModule} from "ngx-autosize";

@Component({
  selector: 'app-create-doctor-quest',
  templateUrl: './create-doctor-quest-dialog.component.html',
  styleUrls: ['./create-doctor-quest-dialog.component.css']
})
export class CreateDoctorQuestDialog implements OnInit {

  doctor: any;

  questForm = new FormGroup({
    name: new FormControl(),
    description: new FormControl(),
    exp_reward: new FormControl(),
    gold_reward: new FormControl(),
    repetition_cycle_days : new FormControl(),
    exp_penalty: new FormControl(),
    gold_penalty: new FormControl(),
    email: new FormControl(),
    password: new FormControl()
  })

  name = '';
  description = '';
  exp_reward = 0;
  gold_reward = 0;
  repetition_cycle_days =0;
  exp_penalty = 0;
  gold_penalty = 0;
  email = '';
  password = '';


  constructor(public dialogRef: MatDialogRef<CreateDoctorQuestDialog>, private route: ActivatedRoute, private dialog: MatDialog, private questService: QuestService, private location: Location) {
  }

  ngOnInit(): void {
  }

  /**
   * Creates a new DoctorQuest and sends it to the backend.
   */
  createDoctorQuest() {
    let repetition_cycle: Duration = moment.duration({
      days: this.repetition_cycle_days
    });
    let credentials: Credentials = {email: this.email, password:  this.password};
    let quest: Quest = {id: 0, name: this.name, description: this.description, exp_reward: this.exp_reward, gold_reward: this.gold_reward, repetition_cycle: repetition_cycle.toISOString(), exp_penalty: this.exp_penalty, gold_penalty: this.gold_penalty, doctor: Number(sessionStorage.getItem('id')), dueDate: new Date()};
    let createDoctorQuest: CreateDoctorQuest = {credentials: credentials, quest: quest};
    this.questService.createDoctorQuest(createDoctorQuest).subscribe(
      (quest : Quest) => {
        this.dialogRef.close();
      },
      error => {
        this.defaultServiceErrorHandling(error);
      });
  }





  /**
   * Shows an error message if an error occurs
   * @param error that should be displayed
   */
  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    this.dialog.open(ErrorDialogComponent, {
      data: { err: error, message: '' }
    });
  }
}
