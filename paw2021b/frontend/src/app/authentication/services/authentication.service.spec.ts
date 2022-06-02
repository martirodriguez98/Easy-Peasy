import {getTestBed, TestBed} from '@angular/core/testing';

import {AuthenticationService, RegisterData} from './authentication.service';
import {UserService} from "./user.service";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpStatusCode} from "@angular/common/http";
import {environment} from "../../../environments/environment";

describe('AuthenticationService', () => {
  let service: AuthenticationService;
  let injector: TestBed;
  let authenticationService: AuthenticationService;
  let userService: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule,
        RouterTestingModule.withRoutes(
          [{path: 'login', redirectTo: ''}]
        )
      ],
      providers: [AuthenticationService, UserService]
    });
    injector = getTestBed();
    authenticationService = injector.inject(AuthenticationService);
    userService = injector.inject(UserService);
    httpMock = injector.inject(HttpTestingController);
    userService.user.next({...userService.user.getValue(), roles: ['VERIFIED'], id: 1})
    service = TestBed.inject(AuthenticationService);
  });

  it('register() should return created', () => {
    let userData: RegisterData = {
      username: "username",
      email: "email",
      password: "password"
    }

    authenticationService.register(userData).subscribe(
      (res) => {
        expect(res.status).toEqual(HttpStatusCode.Created)
      }
    )

    const req = httpMock.expectOne(environment.apiBaseUrl + '/users');
    expect(req.request.method).toBe('POST');

    req.flush(userData,
      {
        status: HttpStatusCode.Created,
        statusText: HttpStatusCode.Created.toString()
      });

  });

  it('login() should return created', () => {

    authenticationService.login("username", "password").subscribe(
      (res) => {
        expect(res.status).toEqual(HttpStatusCode.Created)
      }
    )

    const req = httpMock.expectOne(environment.apiBaseUrl + '/login');
    expect(req.request.method).toBe('POST');

  });

  it('logout() should clear session', () => {

    authenticationService.logout();

    expect(authenticationService.session.getValue()).toBe(null);
  });

  it('resetPassword() should return no content', () => {
    authenticationService.resetPassword("PASSWORD_RESET_TOKEN", "new_password")
      .subscribe(
        (res) => {
          expect(res.status).toEqual(HttpStatusCode.NoContent)
        }
      );

    const req = httpMock.expectOne(environment.apiBaseUrl + '/users/passwordReset');
    expect(req.request.method).toBe('PUT');

    req.flush({},
      {
        status: HttpStatusCode.NoContent,
        statusText: HttpStatusCode.Created.toString()
      });
  });

  it('resendVerification() should return no content', () => {

    authenticationService.resendVerification()
      .subscribe(
        (res) => {
          expect(res.status).toEqual(HttpStatusCode.NoContent)
        }
      );

    const req = httpMock.expectOne(environment.apiBaseUrl + '/users/verification/');
    expect(req.request.method).toBe('POST');

    req.flush({},
      {
        status: HttpStatusCode.NoContent,
        statusText: HttpStatusCode.Created.toString()
      });
  });

  afterEach(() => {
    httpMock.verify();
  });

});
