import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Doctor} from '../dto/doctor';
import {Quest} from '../dto/quest';
import {environment} from '../../environments/environment';
import {AcceptedQuest} from '../dto/accepted-quest';

const baseUri = environment.backendUrl + '/quests';

@Injectable({
  providedIn: 'root'
})
export class QuestService {

  private httpOption = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

  constructor(private httpClient: HttpClient) { }

  /**
   * Get all the available doctor quests that can be assigned to a user
   * @param doctor that assigns the quests
   * @param user that has the quests assigned to
   */
  getAvailableDoctorQuestsForUser(doctor: number, user: number): Observable<Quest[]> {
    console.log('getDoctorAssignedQuestsForUser(' + doctor + ', ' + user + ')');
    let availableParams = new HttpParams();
    availableParams = availableParams.set('user', String(user));
    availableParams = availableParams.set('doctor', String(doctor));
    console.log(availableParams);
    return this.httpClient.get<Quest[]>(baseUri + '/available?' + availableParams);
  }

  /**
   * Get all the assigned doctor quests are assigned to a user
   * @param doctor that assigns the quests
   * @param user that has the quests assigned to
   */
  getAssignedDoctorQuestsForUser(doctor: number, user: number): Observable<Quest[]> {
    console.log('getDoctorAssignedQuestsForUser(' + doctor + ', ' + user + ')');
    let assignedParams = new HttpParams();
    assignedParams = assignedParams.set('user', String(user));
    assignedParams = assignedParams.set('doctor', String(doctor));
    console.log(assignedParams);
    return this.httpClient.get<Quest[]>(baseUri + '/assigned?' + assignedParams);
  }

  /**
   * Delete an assigned doctor quest for a user
   * @param quest the quest id
   * @param user that the quest is assigned to
   */
  deleteAssignedDoctorQuestForUser(quest: number, user: number): Observable<boolean>{
    console.log('deleteAssignedDoctorQuestForUser(' + quest + ', ' + user + ')');
    return this.httpClient.delete<boolean>(baseUri + '/assigned/' + quest + '/' + user);
  }

  /**
   * Adds a new doctor quest for a user
   * @param accQuest the quest that should be added
   */
  addAssignedDoctorQuestForUser(accQuest: AcceptedQuest): Observable<boolean>{
    console.log('addAssignedDoctorQuestForUser(' + accQuest + ')');
    const acceptedBody = JSON.stringify(accQuest);
    return this.httpClient.post<boolean>(baseUri + '/assigned', acceptedBody, this.httpOption);
  }
}
