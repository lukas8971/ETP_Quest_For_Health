import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dto/user';
import {environment} from '../../environments/environment';
import {Credentials} from "../dto/credentials";
import {Doctor} from "../dto/doctor";
import {CharacterLevel} from "../dto/character-level";
import {Equipment} from "../dto/equipment";

const baseUri = environment.backendUrl + '/equipment';

@Injectable({
  providedIn: 'root'
})
export class EquipmentService {

  private httpOption = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

  constructor(private httpClient: HttpClient) { }

  getEquipmentWornByUserId(id: number): Observable<Equipment[]> {
    console.log('getEquipmentWornByUserId');
    return this.httpClient.get<Equipment[]>(baseUri + '/wornByUser/' + id);
  }
}
