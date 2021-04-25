import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDoctorQuestComponent } from './create-doctor-quest.component';

describe('CreateDoctorQuestComponent', () => {
  let component: CreateDoctorQuestComponent;
  let fixture: ComponentFixture<CreateDoctorQuestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateDoctorQuestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateDoctorQuestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
