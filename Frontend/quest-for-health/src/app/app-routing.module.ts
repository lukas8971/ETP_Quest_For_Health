import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DoctorOverviewComponent} from './component/doctor-overview/doctor-overview.component';
import {PatientDoctorQuestComponent} from './component/patient-doctor-quest/patient-doctor-quest.component';
import {DoctorComponent} from './component/doctor/doctor.component';

const routes: Routes = [
  {path: 'doctors/overview', component: DoctorOverviewComponent},
  {path: 'doctors/patientquest', component: PatientDoctorQuestComponent},
  {path: 'doctors', component: DoctorComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
