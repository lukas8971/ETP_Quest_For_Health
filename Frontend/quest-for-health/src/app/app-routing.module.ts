import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DoctorOverviewComponent} from './component/doctor-overview/doctor-overview.component';
import {PatientDoctorQuestComponent} from './component/patient-doctor-quest/patient-doctor-quest.component';
import {DoctorComponent} from './component/doctor/doctor.component';
import {DoctorLogoutComponent} from './component/doctor-logout/doctor-logout.component';
import {AuthGuardDoctorService} from './service/auth-guard-doctor.service';
import {CreateCharacterComponent} from "./component/create-character/create-character.component";
import {UserLoginComponent} from "./component/user-login/user-login.component";
import {UserOverviewComponent} from "./component/user-overview/user-overview.component";
import {AuthGuardUserService} from "./service/auth-guard-user.service";
import {UserProfileComponent} from "./component/user-profile/user-profile.component";

const routes: Routes = [
  {path: 'doctors/overview', component: DoctorOverviewComponent, canActivate: [AuthGuardDoctorService]},
  {path: 'doctors/patientquest', component: PatientDoctorQuestComponent, canActivate: [AuthGuardDoctorService]},
  {path: 'doctors', component: DoctorComponent},
  {path: 'doctors/logout', component: DoctorLogoutComponent, canActivate: [AuthGuardDoctorService]},
  {path: 'createCharacter', component: CreateCharacterComponent},
  {path: 'userLogin', component: UserLoginComponent},
  {path: 'users/overview', component: UserOverviewComponent, canActivate: [AuthGuardUserService]},
  {path: 'users/profile', component: UserProfileComponent, canActivate: [AuthGuardUserService]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
