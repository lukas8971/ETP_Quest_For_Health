import { Component, OnInit } from '@angular/core';
import {StoryChapter} from '../../dto/story-chapter';
import {MatDialog} from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material/snack-bar';
import {StoryService} from '../../service/story.service';
import {ErrorDialogComponent} from '../error-dialog/error-dialog.component';
import {UserService} from '../../service/user.service';
import {User} from '../../dto/user';

@Component({
  selector: 'app-story',
  templateUrl: './story.component.html',
  styleUrls: ['./story.component.css']
})
export class StoryComponent implements OnInit {

  userId = -1;
  user: any;
  currentChapter: any;
  previousChapters: any[] = [];
  displayChapter: any;
  loaded = false;

  constructor(private dialog: MatDialog, private storyService: StoryService, private snackBar: MatSnackBar,
              private userService: UserService) { }

  ngOnInit(): void {
    this.userId = Number(sessionStorage.getItem('userId'));
    this.getCurrentUser();
  }

  /**
   * Display previous chapter
   */
  public prev(): void{
    console.log('prev');
    if (this.displayChapter.prevChapter === null) {
      this.snackBar.open('This is already the first chapter.', 'OK');
    } else {
      this.displayChapter = this.previousChapters.find(x => x.id === this.displayChapter.prevChapter);
      this.getPrevChapters();
    }
  }

  /**
   * Display next chapter
   */
  public next(): void{
    console.log('prev');
    if (this.displayChapter.nextChapter === null) {
      this.snackBar.open('This already is your last chapter.' + '\nGain more strength to get more!', 'Great');
    } else {
      this.displayChapter = this.previousChapters.find(x => x.id === this.displayChapter.nextChapter);
      this.getPrevChapters();
    }
  }

  /**
   * When the select changed
   */
  public changed(event: any): void{
    console.log(event);
    this.displayChapter = this.previousChapters.find(x => x.name === event);
    this.getPrevChapters();
  }

  /**
   * Gets the current user
   */
  private getCurrentUser(): void {
    console.log('getCurrentUser');
    this.userService.getUserById(this.userId).subscribe(
      (u: User) => {
        this.user = u;
        this.getCurrentStory();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Gets the current story chapter of the user
   */
  private getCurrentStory(): void {
    console.log('getCurrentStory');
    this.storyService.getOneById(this.user.storyChapter).subscribe(
      (sc: StoryChapter) => {
        this.currentChapter = sc;
        this.displayChapter = this.currentChapter;
        this.getPrevChapters();
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Gets the previous already done chapters of the user
   */
  private getPrevChapters(): void {
    console.log('getPrevChapters');
    this.storyService.getAllPreviousChaptersOfUser(this.userId).subscribe(
      (c: StoryChapter[]) => {
        this.previousChapters = c;
        this.loaded = true;
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Shows an error message if an error occurs
   * @param error that should be displayed
   */
  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    this.dialog.open(ErrorDialogComponent, {
      data: { err: error, message: '' }
    });
  }
}
