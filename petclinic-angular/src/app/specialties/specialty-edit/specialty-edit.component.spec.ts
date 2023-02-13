import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpecialtyEditComponent } from './specialty-edit.component';

describe('SpecialtyEditComponent', () => {
  let component: SpecialtyEditComponent;
  let fixture: ComponentFixture<SpecialtyEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpecialtyEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpecialtyEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
