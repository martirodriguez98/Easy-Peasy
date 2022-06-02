import { NgModule } from '@angular/core';
import {APP_BASE_HREF} from '@angular/common';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {AuthInterceptorService} from "./authentication/services/authentication-interceptor.service";
import {environment} from "../environments/environment";

@NgModule({
  providers : [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    },
    {
      provide: APP_BASE_HREF,
      useValue: environment.baseHref
    }
  ]
})
export class CoreModule { }
