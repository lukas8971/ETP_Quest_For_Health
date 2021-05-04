import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Quest} from '../dto/quest';
import {environment} from '../../environments/environment';
import {AcceptedQuest} from '../dto/accepted-quest';
import {CreateDoctorQuest} from "../dto/createDoctorQuest";
import {CompletedQuest} from "../dto/completed-quest";

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
   * Get all new normal quests for user
   * @param user for which to find new quests
   */
  getNewQuestsForUser(user: number): Observable<Quest[]> {
    console.log('getNewQuestsForUser('+ user + ')');
    let newQuestParams = new HttpParams();
    newQuestParams = newQuestParams.set('user', String(user));
    return this.httpClient.get<Quest[]>(baseUri + '/newQuests?' + newQuestParams);
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

  acceptQuest(user: number, quest: number): Observable<boolean> {
    console.log('acceptQuest (' +user+ ", " + quest+ ')');
    let acceptParams = new HttpParams();
    acceptParams = acceptParams.set('user', String(user));
    acceptParams = acceptParams.set('quest', String(quest));
    return this.httpClient.get<boolean>(baseUri + '/accept?' + acceptParams);
  }

  /**
   * Creates a new doctor-quest in the backend.
   * @param createDoctorQuest the doctor-quest to create with credentials.
   */
  createDoctorQuest(createDoctorQuest: CreateDoctorQuest): Observable<Quest> {
    console.log('createDoctorQuest(' + createDoctorQuest + ')');
    return this.httpClient.post<Quest>(baseUri +'/', createDoctorQuest);
  }

  /**
   * Gets all accepted quests for a user.
   * @param user the user to get the quests for
   */
  getAcceptedQuestsForUser(user: number):Observable<AcceptedQuest[]> {
    console.log('getAcceptedQuestsForUser('+user+')');
    return this.httpClient.get<AcceptedQuest[]>(baseUri + '/accepted/' + user);
  }

  /**
   * Gets all completed quests for a user.
   * @param user the user to get the quests for
   */
  getCompletedQuestsForUser(user: number):Observable<CompletedQuest[]> {
    console.log('getCompletedQuestsForUser('+user+')');
    return this.httpClient.get<CompletedQuest[]>(baseUri + '/completed/' + user);
  }

  /**
   * Gets a Quest by it's id.
   * @param id the id of the quest
   */
  getQuestById(id: number):Observable<Quest> {
    console.log('getQuestById({})',id);
    return this.httpClient.get<Quest>(baseUri + '/' + id);
  }

  getAllQuestsDueForUser(user:number): Observable<Quest[]>{
    console.log('getAllQuestsDueForUser(' +user+')');
    return this.httpClient.get<Quest[]>(baseUri+'/dueQuests/'+user);
  }
  getAllMissedQuestsForUser(user:number): Observable<Quest[]>{
    console.log('getAllMissedQuestsForUser(' +user+')');
    return this.httpClient.get<Quest[]>(baseUri+'/missedQuests/'+user);
  }

  getAllOpenOneTimeQuestsForUser(user:number): Observable<Quest[]>{
    console.log('getAllOpenOneTimeQuestsForUser('+user+')');
    return this.httpClient.get<Quest[]>(baseUri+ '/openOneTimeQuests/'+user);
  }

}
