import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DoctorOverviewComponent } from './component/doctor-overview/doctor-overview.component';
import { HeaderComponent } from './component/header/header.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatTableModule} from '@angular/material/table';
import {MatButtonModule} from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ErrorDialogComponent } from './component/error-dialog/error-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';
import { PatientDoctorQuestComponent } from './component/patient-doctor-quest/patient-doctor-quest.component';
import { DoctorComponent } from './component/doctor/doctor.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { DoctorLogoutComponent } from './component/doctor-logout/doctor-logout.component';
import {CommonModule} from '@angular/common';
import {MatCommonModule} from '@angular/material/core';
import {MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import { CreateDoctorQuestComponent } from './component/create-doctor-quest/create-doctor-quest.component';

@NgModule({
  declarations: [
    AppComponent,
    DoctorOverviewComponent,
    HeaderComponent,
    ErrorDialogComponent,
    PatientDoctorQuestComponent,
    DoctorComponent,
    DoctorLogoutComponent,
    CreateDoctorQuestComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatIconModule,
    MatTableModule,
    MatButtonModule,
    MatSnackBarModule,
    MatDialogModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    MatCommonModule,
    MatCardModule,
    MatInputModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  exports: []
})
export class AppModule { }
