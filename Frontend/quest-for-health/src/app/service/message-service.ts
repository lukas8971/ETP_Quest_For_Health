import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { Message } from "../entity/message";

@Injectable({
  providedIn: 'root'
})
export class MessagesService {
  constructor() { }

  private subject = new Subject<any>();

  sendMessage(message: Message) {
    this.subject.next(message);
  }

  subscribeToMessagesChannel(): Observable<Message> {
    return this.subject.asObservable();
  }
}
