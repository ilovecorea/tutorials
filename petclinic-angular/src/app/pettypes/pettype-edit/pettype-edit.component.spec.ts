import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PettypeEditComponent } from './pettype-edit.component';

describe('PettypeEditComponent', () => {
  let component: PettypeEditComponent;
  let fixture: ComponentFixture<PettypeEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PettypeEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PettypeEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
