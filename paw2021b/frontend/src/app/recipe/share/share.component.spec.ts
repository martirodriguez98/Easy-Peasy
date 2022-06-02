import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShareComponent } from './share.component';
import {TestingModule} from "../../testing.module";

describe('ShareComponent', () => {
  let component: ShareComponent;
  let fixture: ComponentFixture<ShareComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShareComponent ],
      imports: [
        TestingModule
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
