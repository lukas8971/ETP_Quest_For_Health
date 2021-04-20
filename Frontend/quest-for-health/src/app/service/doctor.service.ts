import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Doctor} from '../dto/doctor';
import {Credentials} from '../dto/credentials';

const baseUri = environment.backendUrl + '/doctors';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {

  private httpOption = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

  constructor(private httpClient: HttpClient) { }

  /**
   * Get a list of all doctors
   */
  getAllDoctors(): Observable<Doctor[]> {
    console.log('getAllDoctors()');
    return this.httpClient.get<Doctor[]>(baseUri);
  }

  /**
   * Get the information about one doctor
   * @param id of the doctor to get the information for
   */
  getDoctorById(id: number): Observable<Doctor> {
    console.log('getDoctor');
    return this.httpClient.get<Doctor>(baseUri + '/' + id);
  }

  /**
   * Checks the login data for a doctor is valid
   * @param cred Credentials (email, password) for a doctor
   */
  checkLogin(cred: Credentials): Observable<Doctor>{
    console.log('Login');
    return this.httpClient.post<Doctor>(baseUri + '/login', JSON.stringify(cred), this.httpOption);
  }
}
