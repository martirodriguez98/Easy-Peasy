import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetPasswordComponent } from './reset-password.component';
import {TestingModule} from "../../testing.module";
import {ReactiveFormsModule} from "@angular/forms";

describe('ResetPasswordComponent', () => {
  let component: ResetPasswordComponent;
  let fixture: ComponentFixture<ResetPasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ResetPasswordComponent ],
      imports: [
        TestingModule, ReactiveFormsModule
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
