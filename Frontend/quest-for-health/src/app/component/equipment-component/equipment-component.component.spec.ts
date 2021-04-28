import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EquipmentComponentComponent } from './equipment-component.component';

describe('EquipmentComponentComponent', () => {
  let component: EquipmentComponentComponent;
  let fixture: ComponentFixture<EquipmentComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EquipmentComponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EquipmentComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
