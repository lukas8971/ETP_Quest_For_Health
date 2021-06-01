import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {User} from '../../dto/user';
import {UserService} from '../../service/user.service';
import {ErrorDialogComponent} from '../error-dialog/error-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {faTrophy} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent implements OnInit, AfterViewInit {

  // @ts-ignore
  @ViewChild(MatPaginator) paginator: MatPaginator;

  Cols: string[] = ['place', 'char', 'stren', 'exp'];
  success = false;
  users: any[] = [];
  dataSource = new MatTableDataSource(this.users);
  userId = 0;
  yourPage = 0;
  level = 0;

  faTrophy = faTrophy;

  constructor(private userService: UserService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.userId = Number(sessionStorage.getItem('userId'));
    this.getLeaderboard();
  }

  ngAfterViewInit(): void {
  }

  /**
   * Gets the leaderboard of the current user
   */
  private getLeaderboard(): void {
    console.log('getLeaderboard');
    this.userService.getLeaderboard(this.userId).subscribe(
      (u: User[]) => {
        if (u !== undefined) {
          this.users = u;
          this.level = this.users.find(l => l.id === this.userId).characterLevel;
          this.dataSource = new MatTableDataSource(this.users);
          const a = JSON.parse(JSON.stringify(u)); // cloning... TS
          let i = 0;
          while (a.length) {
            const x = a.splice(0, this.paginator.pageSize);
            const found = x.find((y: User) => y.id === this.userId);
            if (found !== undefined && found.id === this.userId) {
              this.yourPage = i;
              break;
            }
            i++;
          }
        }
        this.paginator.pageIndex = this.yourPage;
        this.dataSource.paginator = this.paginator;
        this.success = true;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Showsan error message if an error occurs
   * @param error that should be displayed
   */
  private defaultServiceErrorHandling(error: any): void {
    console.log(error);
    this.dialog.open(ErrorDialogComponent, {
      data: { err: error, message: '' }
    });
  }
}
