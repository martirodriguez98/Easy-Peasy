import {getTestBed, TestBed} from '@angular/core/testing';

import { AuthInterceptorService } from './authentication-interceptor.service';
import {TestingModule} from "../../testing.module";
import {ReactiveFormsModule} from "@angular/forms";
import {AuthenticationService} from "./authentication.service";
import {HttpTestingController} from "@angular/common/http/testing";

describe('AuthenticationInterceptorService', () => {
  let service: AuthInterceptorService;
  let injector: TestBed;
  let httpMock: HttpTestingController;
  let authService: AuthenticationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        TestingModule, ReactiveFormsModule
      ],
      providers: [
        AuthenticationService, AuthInterceptorService,
      ]
    });
    injector = getTestBed();
    httpMock = injector.inject(HttpTestingController);
    authService = injector.inject(AuthenticationService);
    service = TestBed.inject(AuthInterceptorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
