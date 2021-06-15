import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dto/user';
import {environment} from '../../environments/environment';
import {Credentials} from "../dto/credentials";
import {Doctor} from "../dto/doctor";
import {CharacterLevel} from "../dto/character-level";

const baseUri = environment.backendUrl + '/characterLevels';

@Injectable({
  providedIn: 'root'
})
export class CharacterLevelService {

  private httpOption = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

  constructor(private httpClient: HttpClient) { }

  getCharacterLevelById(id: number): Observable<CharacterLevel> {
    console.log('getCharacterLevelById');
    return this.httpClient.get<CharacterLevel>(baseUri + '/' + id);
  }
  getCharacterLevelByLevel(level: number): Observable<CharacterLevel> {
    console.log('getCharacterLevelByLevel');
    return this.httpClient.get<CharacterLevel>(baseUri + '/level/' + level);
  }
  getCharacterNextLevel(id: number): Observable<CharacterLevel> {
    console.log('getCharacterNextLevel');
    return this.httpClient.get<CharacterLevel>(baseUri + '/next/' + id);
  }
}
