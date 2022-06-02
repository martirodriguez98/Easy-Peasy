import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExtraLayoutComponent } from './extra-layout.component';
import {TestingModule} from "../../testing.module";
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";

describe('ExtraLayoutComponent', () => {
  let component: ExtraLayoutComponent;
  let fixture: ComponentFixture<ExtraLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExtraLayoutComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      imports: [
        TestingModule
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExtraLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
