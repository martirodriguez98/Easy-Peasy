import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerificationNeededComponent } from './verification-needed.component';
import {TestingModule} from "../../../testing.module";

describe('VerificationNeededComponent', () => {
  let component: VerificationNeededComponent;
  let fixture: ComponentFixture<VerificationNeededComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerificationNeededComponent ],
      imports: [
        TestingModule
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VerificationNeededComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
