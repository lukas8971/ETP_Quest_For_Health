import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Doctor} from '../dto/doctor';
import {Credentials} from '../dto/credentials';
import {DoctorUserRelation} from '../dto/doctor-user-relation';

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

  /**
   * Assignes a new patient to the doctor
   * @param rel Doctor user relationship that should be added
   */
  assignNewPatient(rel: DoctorUserRelation): Observable<DoctorUserRelation>{
    console.log('assignNewPatient');
    return this.httpClient.post<DoctorUserRelation>(baseUri + '/relation', JSON.stringify(rel), this.httpOption);
  }

  /**
   * Removes a patient to the doctor
   * @param rel Doctor user relationship that should be removed
   */
  removePatient(rel: DoctorUserRelation): Observable<boolean>{
    console.log('assignNewPatient');
    return this.httpClient.delete<boolean>(baseUri + '/relation/' + rel.docId + '/' + rel.userId);
  }
}
