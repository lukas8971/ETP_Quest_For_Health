import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientDoctorQuestComponent } from './patient-doctor-quest.component';

describe('PatientDoctorQuestComponent', () => {
  let component: PatientDoctorQuestComponent;
  let fixture: ComponentFixture<PatientDoctorQuestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PatientDoctorQuestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PatientDoctorQuestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
