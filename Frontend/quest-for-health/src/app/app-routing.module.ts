import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DoctorOverviewComponent} from './component/doctor-overview/doctor-overview.component';
import {PatientDoctorQuestComponent} from './component/patient-doctor-quest/patient-doctor-quest.component';
import {DoctorComponent} from './component/doctor/doctor.component';
import {DoctorLogoutComponent} from './component/doctor-logout/doctor-logout.component';
import {AuthGuardDoctorService} from './service/auth-guard-doctor.service';

const routes: Routes = [
  {path: 'doctors/overview', component: DoctorOverviewComponent, canActivate: [AuthGuardDoctorService]},
  {path: 'doctors/patientquest', component: PatientDoctorQuestComponent, canActivate: [AuthGuardDoctorService]},
  {path: 'doctors', component: DoctorComponent},
  {path: 'doctors/logout', component: DoctorLogoutComponent, canActivate: [AuthGuardDoctorService]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
