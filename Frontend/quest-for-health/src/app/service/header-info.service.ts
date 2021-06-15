import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {User} from '../dto/user';

@Injectable({
  providedIn: 'root'
})
export class HeaderInfoService {

  user = new BehaviorSubject<User>({id: -1, firstname: '', lastname: '', characterName: '',
  characterStrength: -1, characterLevel: -1, password: '', email: '', storyChapter: -1, characterExp: -1, characterGold: -1});

  setUser(user: User): void {
    this.user.next(user);
  }

  constructor() { }
}
