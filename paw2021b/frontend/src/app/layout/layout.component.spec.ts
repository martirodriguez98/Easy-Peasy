import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LayoutComponent } from './layout.component';
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {TestingModule} from "../testing.module";

describe('LayoutComponent', () => {
  let component: LayoutComponent;
  let fixture: ComponentFixture<LayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LayoutComponent ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      imports: [
        TestingModule
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
