import {Component, Input, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {User} from '../../dto/user';
import {StoryService} from '../../service/story.service';
import {CharacterLevelService} from '../../service/character-level.service';
import {UserService} from '../../service/user.service';
import {StoryChapter} from '../../dto/story-chapter';
import {CharacterLevel} from '../../dto/character-level';
import {faCoins, faDiceD20, faFistRaised, faScroll} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-user-info',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {

  @Input() user: User | undefined;

  stDone = false;
  lvDone = false;
  strength = -1;
  faCoins = faCoins;
  faStrength = faFistRaised;
  faExp = faDiceD20;
  faStory = faScroll;

  constructor(private storyService: StoryService, private levelService: CharacterLevelService, private userService: UserService) { }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnChanges(...args: any[]): void {
    console.log('Change user info');
    if (this.user !== undefined && this.user.id > 0) {
      this.setStrength();
      this.setLv();
    }
  }

  ngOnInit(): void {
  }

  private setStrength(): void{
    if (this.user !== undefined) {
      this.userService.getUserStrength(this.user.id).subscribe(
        (stren: number) => {
          if (this.user !== undefined) {
            this.storyService.getNextChapterInfoOfUser(this.user.id).subscribe(
              (sto: StoryChapter) => {
                const p = (stren / sto.strengthRequirement) * 100;
                this.strength = stren;
                const st = document.getElementById('progress-bar-st-percentage');
                const htmlStren = document.getElementById('strength');
                if (htmlStren !== null) {
                  htmlStren.textContent = String(stren) + ' / ' + String(sto.strengthRequirement) + ' st';
                }
                if (st !== null) {
                  st.style.width = p + '%';
                }
                this.stDone = true;
              }, error => {
                // this.defaultServiceErrorHandling(error);
              }
            );
          }
        }, error => {
          // this.defaultServiceErrorHandling(error);
        }
      );
    }
  }

  private setLv(): void{
    if (this.user !== undefined) {
      this.levelService.getCharacterNextLevel(this.user.characterLevel).subscribe(
        (level: CharacterLevel) => {
          if (this.user !== undefined) {
            const p = (this.user.characterExp / level.neededExp) * 100;
            const lv = document.getElementById('progress-bar-lv-percentage');
            const htmlLevel = document.getElementById('level');
            if (htmlLevel !== null) {
              htmlLevel.textContent = String(this.user.characterExp) + ' / ' + String(level.neededExp) + ' xp';
            }
            if (lv !== null) {
              lv.style.width = p + '%';
            }
            this.lvDone = true;
          }
        }, error => {
          // this.defaultServiceErrorHandling(error);
        }
      );
    }
  }
}
