import { NgModule } from '@angular/core';
import { LoginComponent } from './login.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { LoginRoutingModule } from './login-routing.module';
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {CommonModule} from "@angular/common";
import {ReqResetPassComponent} from "./request-reset-password/req-reset-pass.component";

@NgModule({
  declarations: [
    LoginComponent,
    ReqResetPassComponent
  ],
    imports: [
        SharedModule,
        LoginRoutingModule,
        TranslateModule,
        ReactiveFormsModule,
        FontAwesomeModule,
        CommonModule,
    ],
  exports: [
    LoginComponent
  ]
})
export class LoginModule { }
