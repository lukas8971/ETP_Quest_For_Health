import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserBrowseQuestsComponent } from './user-browse-quests.component';

describe('UserBrowseQuestsComponent', () => {
  let component: UserBrowseQuestsComponent;
  let fixture: ComponentFixture<UserBrowseQuestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserBrowseQuestsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserBrowseQuestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
