import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Doctor} from '../dto/doctor';

const baseUri = environment.backendUrl + '/doctors';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {

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
}
