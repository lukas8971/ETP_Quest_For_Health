import { TestBed } from '@angular/core/testing';

import { DoctorService } from './doctor.service';
import {environment} from '../../environments/environment';

const baseUri = environment.backendUrl + '/doctors';

describe('DoctorService', () => {
  let service: DoctorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DoctorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
