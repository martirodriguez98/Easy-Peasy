import {getTestBed, TestBed} from '@angular/core/testing';

import { UserService } from './user.service';
import {HttpTestingController} from "@angular/common/http/testing";
import {User} from "../../model/user.model";
import {TestingModule} from "../../testing.module";
import {ReactiveFormsModule} from "@angular/forms";
import {HttpStatusCode} from "@angular/common/http";
import {environment} from "../../../environments/environment";

describe('UserService', () => {
  let service: UserService;
  let injector: TestBed;
  let httpMock: HttpTestingController;
  let mockUser: User = new User(
    ['USER'], 1, 'surname', 'email', null, '', 0, 0
  );
  const mockImage = new FormData();

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        TestingModule, ReactiveFormsModule
      ],
      providers: [
        UserService
      ],
    });
    injector = getTestBed();
    httpMock = injector.inject(HttpTestingController);
    service = TestBed.inject(UserService);
    service.user.next({...service.user.getValue(), id: 1, roles: ['USER']})
  });

  it('fillUserData() should return user', () => {
    service.fillUserData().subscribe();
    const req = httpMock.expectOne(environment.apiBaseUrl + '/users/' + service.user.getValue().id);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser, {status: HttpStatusCode.Created, statusText: HttpStatusCode.Created.toString()});
    expect(service.user.value).toEqual({...mockUser});
  });

  it('getUser() should return a user', () => {
    service.getUser(mockUser.id).subscribe((res) => {
      expect(res).toEqual(mockUser);
    });

    const req = httpMock.expectOne(environment.apiBaseUrl + '/users/' + mockUser.id);

    expect(req.request.method).toBe('GET');

    req.flush(mockUser, {status: HttpStatusCode.Created, statusText: HttpStatusCode.Created.toString()});
  });

  it('ban() should return OK', () => {
    service.ban(mockUser.id).subscribe();

    const req = httpMock.expectOne(environment.apiBaseUrl + '/users/banned/' + mockUser.id);

    expect(req.request.method).toBe('PUT');

    req.flush({}, {
      status: HttpStatusCode.Ok,
      statusText: HttpStatusCode.Created.toString()
    })
  });

  it('unban() should return OK', () => {
    service.ban(mockUser.id).subscribe();

    const req = httpMock.expectOne(environment.apiBaseUrl + '/users/banned/' + mockUser.id);

    expect(req.request.method).toBe('PUT');

    req.flush({}, {
      status: HttpStatusCode.Ok,
      statusText: HttpStatusCode.Created.toString()
    })
  });

  it('makeAdmin() should return OK', () => {
    service.makeAdmin(mockUser.id).subscribe();

    const req = httpMock.expectOne(environment.apiBaseUrl + '/users/admin/' + mockUser.id);

    expect(req.request.method).toBe('PUT');

    req.flush({}, {
      status: HttpStatusCode.Ok,
      statusText: HttpStatusCode.Created.toString()
    })
  });

  it('removeAdmin() should return OK', () => {
    service.makeAdmin(mockUser.id).subscribe();

    const req = httpMock.expectOne(environment.apiBaseUrl + '/users/admin/' + mockUser.id);

    expect(req.request.method).toBe('PUT');

    req.flush({}, {
      status: HttpStatusCode.Ok,
      statusText: HttpStatusCode.Created.toString()
    })
  });

  afterEach(() => {
    httpMock.verify();
  });
});
