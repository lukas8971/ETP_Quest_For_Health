import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDoctorQuestDialog } from './create-doctor-quest-dialog.component';

describe('CreateDoctorQuestComponent', () => {
  let component: CreateDoctorQuestDialog;
  let fixture: ComponentFixture<CreateDoctorQuestDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateDoctorQuestDialog ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateDoctorQuestDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
