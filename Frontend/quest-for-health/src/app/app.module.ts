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
import { CreateCharacterComponent } from './component/create-character/create-character.component';
import { UserLoginComponent } from './component/user-login/user-login.component';
import {MissedQuestsDialog, UserOverviewComponent} from './component/user-overview/user-overview.component';
import { UserProfileComponent } from './component/user-profile/user-profile.component';
import {MatGridListModule} from "@angular/material/grid-list";
import {MatExpansionModule} from "@angular/material/expansion";
import { UserBrowseQuestsComponent } from './component/user-browse-quests/user-browse-quests.component';
import {MatSortModule} from "@angular/material/sort";
import {MatRadioModule} from "@angular/material/radio";
import {MatListModule} from "@angular/material/list";
import { CreateDoctorQuestDialog } from './component/create-doctor-quest/create-doctor-quest-dialog.component';
import { EquippedItemComponent } from './component/equipped-item/equipped-item.component';
import { UserEquipmentComponent } from './component/user-equipment/user-equipment.component';
import {MatTabsModule} from '@angular/material/tabs';
import { EquipmentComponentComponent } from './component/equipment-component/equipment-component.component';
import { ShopComponent } from './component/shop/shop.component';

@NgModule({
  declarations: [
    AppComponent,
    DoctorOverviewComponent,
    HeaderComponent,
    ErrorDialogComponent,
    PatientDoctorQuestComponent,
    DoctorComponent,
    DoctorLogoutComponent,
    CreateCharacterComponent,
    UserLoginComponent,
    UserOverviewComponent,
    UserProfileComponent,
    UserBrowseQuestsComponent,
    CreateDoctorQuestDialog,
    MissedQuestsDialog
    EquippedItemComponent
    UserEquipmentComponent,
    EquipmentComponentComponent,
    ShopComponent,
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
    MatInputModule,
    MatGridListModule,
    MatListModule
    MatRadioModule,
    MatSortModule,
    MatExpansionModule,
    MatTabsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
  exports: []
})
export class AppModule { }
