import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dto/user';
import {environment} from '../../environments/environment';
import {Credentials} from "../dto/credentials";
import {Doctor} from "../dto/doctor";
import {UserQuest} from "../dto/userQuest";

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
    console.log('completeQuest('+ userQuest + ')');
    return this.httpClient.post<User>(baseUri + '/completeQuest', userQuest);
  }

  /**
   * Checks the login data for a user is valid
   * @param cred Credentials (character-name, password) for a user
   */
  checkLogin(cred: Credentials): Observable<User>{
    console.log('Login');
    return this.httpClient.post<User>(baseUri + '/login', JSON.stringify(cred), this.httpOption);
  }
}
