import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReqResetPassComponent } from './req-reset-pass.component';
import {TestingModule} from "../../../testing.module";
import {ReactiveFormsModule} from "@angular/forms";

describe('ReqResetPassComponent', () => {
  let component: ReqResetPassComponent;
  let fixture: ComponentFixture<ReqResetPassComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReqResetPassComponent ],
      imports: [
        TestingModule, ReactiveFormsModule
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReqResetPassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
