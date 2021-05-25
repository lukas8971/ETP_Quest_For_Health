import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dto/user';
import {environment} from '../../environments/environment';
import {Credentials} from '../dto/credentials';
import {Doctor} from '../dto/doctor';
import {UserQuest} from '../dto/userQuest';
import {UserQuests} from '../dto/userQuests';

const baseUri = environment.backendUrl + '/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private httpOption = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

  constructor(private httpClient: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(baseUri);
  }

  getAllUsersFromDoctor(doctorId: number): Observable<User[]> {
    console.log('getAllUsersFromDoctor');
    return this.httpClient.get<User[]>(baseUri + '/doctor/' + doctorId);
  }

  getAllNotUsersFroMDoctor(doctorId: number): Observable<User[]> {
    console.log('getAllNotUsersFroMDoctor');
    return this.httpClient.get<User[]>(baseUri + '/doctorNot/' + doctorId);
  }

  /**
   * Gets the strength of a user including the equipment
   * @param id of the user
   */
  getUserStrength(id: number): Observable<number> {
    console.log('Get user strength');
    return this.httpClient.get<number>(baseUri + '/' + id + '/strength');
  }

  /**
   * Get the information about one user
   * @param id of the user to get the information for
   */
  getUserById(id: number): Observable<User> {
    console.log('getUserById');
    return this.httpClient.get<User>(baseUri + '/' + id);
  }

  createUser(user: User): Observable<User>{
  console.log('createUser');
  return this.httpClient.post<User>(baseUri, user);
  }

  completeQuest(userQuest: UserQuest): Observable<User>{
    console.log('completeQuest(' + userQuest + ')');
    return this.httpClient.post<User>(baseUri + '/completeQuest', userQuest);
  }

  dismissMissedQuests(userQuests: UserQuests): Observable<User>{
    console.log('dismissMissedQuest(' + userQuests + ')');
    return this.httpClient.post<User>(baseUri + '/dismissQuests', userQuests);
  }

  /**
   * Checks the login data for a user is valid
   * @param cred Credentials (character-name, password) for a user
   */
  checkLogin(cred: Credentials): Observable<User>{
    console.log('Login');
    return this.httpClient.post<User>(baseUri + '/login', JSON.stringify(cred), this.httpOption);
  }

  /**
   * Checks if its possible for a user to get to the next chapter
   * AND if its possible, the chapter will update
   * @param userId of the user
   */
  checkUserForNextStoryAndUpdate(userId: number): Observable<boolean>{
    console.log('getPreviousChapterOfUser(' + userId + ')');
    return this.httpClient.get<boolean>(baseUri + '/checkStory/' + userId);
  }
}
