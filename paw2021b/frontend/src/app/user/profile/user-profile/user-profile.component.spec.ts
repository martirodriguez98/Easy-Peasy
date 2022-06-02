import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProfileComponent } from './user-profile.component';
import {TestingModule} from "../../../testing.module";
import {ReactiveFormsModule} from "@angular/forms";
import {UserService} from "../../../authentication/services/user.service";
import {User} from "../../../model/user.model";

describe('UserProfileComponent', () => {
  let component: UserProfileComponent;
  let fixture: ComponentFixture<UserProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserProfileComponent ],
      imports: [
        TestingModule, ReactiveFormsModule
      ],
      providers: [
        UserService
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    const user = new User(['USER'], 1, 'surname', 'email', null, '', 0, 0);
    fixture = TestBed.createComponent(UserProfileComponent);
    component = fixture.componentInstance;
    component.user = user;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
