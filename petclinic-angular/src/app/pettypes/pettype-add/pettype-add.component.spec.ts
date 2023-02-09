import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PettypeAddComponent } from './pettype-add.component';

describe('PettypeAddComponent', () => {
  let component: PettypeAddComponent;
  let fixture: ComponentFixture<PettypeAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PettypeAddComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PettypeAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
