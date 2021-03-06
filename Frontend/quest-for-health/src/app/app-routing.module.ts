import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DoctorOverviewComponent} from './component/doctor-overview/doctor-overview.component';
import {PatientDoctorQuestComponent} from './component/patient-doctor-quest/patient-doctor-quest.component';
import {DoctorComponent} from './component/doctor/doctor.component';
import {DoctorLogoutComponent} from './component/doctor-logout/doctor-logout.component';
import {AuthGuardDoctorService} from './service/auth-guard-doctor.service';
import {CreateCharacterComponent} from './component/create-character/create-character.component';
import {UserLoginComponent} from './component/user-login/user-login.component';
import {UserOverviewComponent} from './component/user-overview/user-overview.component';
import {AuthGuardUserService} from './service/auth-guard-user.service';
import {UserProfileComponent} from './component/user-profile/user-profile.component';
import {UserBrowseQuestsComponent} from './component/user-browse-quests/user-browse-quests.component';
import {CreateDoctorQuestDialog} from './component/create-doctor-quest/create-doctor-quest-dialog.component';
import {ShopComponent} from './component/shop/shop.component';
import {StoryComponent} from './component/story/story.component';
import {DoctorManageComponent} from './component/doctor-manage/doctor-manage.component';
import {LeaderboardComponent} from './component/leaderboard/leaderboard.component';
import {WelcomeComponent} from './component/welcome/welcome.component';

const routes: Routes = [
  {path: 'doctors/overview', component: DoctorOverviewComponent, canActivate: [AuthGuardDoctorService]},
  {path: 'doctors/patientquest', component: PatientDoctorQuestComponent, canActivate: [AuthGuardDoctorService]},
  {path: 'doctors', component: DoctorComponent},
  {path: 'doctors/logout', component: DoctorLogoutComponent, canActivate: [AuthGuardDoctorService]},
  {path: 'createCharacter', component: CreateCharacterComponent},
  {path: 'userLogin', component: UserLoginComponent},
  {path: 'users/overview', component: UserOverviewComponent, canActivate: [AuthGuardUserService]},
  {path: 'users/profile', component: UserProfileComponent, canActivate: [AuthGuardUserService]},
  {path: 'users/browseQuests', component: UserBrowseQuestsComponent, canActivate: [AuthGuardUserService]},
  {path: 'shop', component: ShopComponent, canActivate: [AuthGuardUserService]},
  {path: 'doctors/createquest', component: CreateDoctorQuestDialog, canActivate: [AuthGuardDoctorService]},
  {path: 'story', component: StoryComponent, canActivate: [AuthGuardUserService]},
  {path: 'doctors/manage', component: DoctorManageComponent, canActivate: [AuthGuardDoctorService]},
  {path: 'users/leaderboard', component: LeaderboardComponent, canActivate: [AuthGuardUserService]},
  {path: '', component: WelcomeComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
