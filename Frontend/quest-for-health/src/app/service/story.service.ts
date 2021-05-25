import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {StoryChapter} from '../dto/story-chapter';
import {Picture} from "../dto/picture";

const baseUri = environment.backendUrl + '/stories';

@Injectable({
  providedIn: 'root'
})
export class StoryService {

  constructor(private httpClient: HttpClient) { }

  /**
   * Gets the story chapter with the given id
   * @param id of the story chapter
   */
  getOneById(id: number): Observable<StoryChapter>{
    console.log('getOneById(' + id + ')');
    return this.httpClient.get<StoryChapter>(baseUri + '/' + id);
  }

  /**
   * Gets all the previous (already done) chapters of a user
   * including the current chapter!
   * @param userId of the user
   */
  getAllPreviousChaptersOfUser(userId: number): Observable<StoryChapter[]>{
    console.log('getAllPreviousChaptersOfUser(' + userId + ')');
    return this.httpClient.get<StoryChapter[]>(baseUri + '/allPrev/' + userId);
  }

  /**
   * Gets all the next (NOT already done) chapters of a user
   * NOT including the current chapter!
   * @param userId of the user
   */
  getAllNextChaptersOfUser(userId: number): Observable<StoryChapter[]>{
    console.log('getAllNextChaptersOfUser(' + userId + ')');
    return this.httpClient.get<StoryChapter[]>(baseUri + '/allNext/' + userId);
  }

  /**
   * Gets the next chapter of a user
   * @param userId of the user
   */
  getNextChapterOfUser(userId: number): Observable<StoryChapter>{
    console.log('getNextChapterOfUser(' + userId + ')');
    return this.httpClient.get<StoryChapter>(baseUri + '/next/' + userId);
  }

  /**
   * Gets the next chapter of a user
   * @param userId of the user
   */
  getNextChapterInfoOfUser(userId: number): Observable<StoryChapter>{
    console.log('getNextChapterInfoOfUser(' + userId + ')');
    return this.httpClient.get<StoryChapter>(baseUri + '/nextInfo/' + userId);
  }

  /**
   * Gets the previous chapter of a user
   * @param userId of the user
   */
  getPreviousChapterOfUser(userId: number): Observable<StoryChapter>{
    console.log('getPreviousChapterOfUser(' + userId + ')');
    return this.httpClient.get<StoryChapter>(baseUri + '/prev/' + userId);
  }

  /**
   * Gets the picture for the chapter in base64
   * @param id of the chapter
   */
  getPicture(id: number): Observable<Picture>{
    console.log('getPicture(' + id + ')');
    return this.httpClient.get<Picture>(baseUri + '/pic/' + id);
  }
}
