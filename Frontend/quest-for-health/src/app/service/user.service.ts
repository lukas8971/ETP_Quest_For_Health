import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dto/user';
import {environment} from '../../environments/environment';

const baseUri = environment.backendUrl + '/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

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
}
