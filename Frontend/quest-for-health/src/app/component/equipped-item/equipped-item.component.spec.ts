import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EquippedItemComponent } from './equipped-item.component';

describe('EquippedItemComponent', () => {
  let component: EquippedItemComponent;
  let fixture: ComponentFixture<EquippedItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EquippedItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EquippedItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
