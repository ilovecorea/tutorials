import {Injectable} from '@angular/core';
import {Specialty} from './specialty';
import {Resolve} from '@angular/router';
import {SpecialtyService} from './specialty.service';
import {Observable} from 'rxjs';

@Injectable()
export class SpecResolver implements Resolve<Specialty[]> {

  constructor(private specialtyService: SpecialtyService) { }

  resolve(): Observable<Specialty[]> | Promise<Specialty[]> | Specialty[] {
    return this.specialtyService.getSpecialties();
  }

}
