import { NgModule } from '@angular/core';
import { ResetPasswordComponent } from './reset-password.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { ResetPasswordRoutingModule } from './reset-password-routing.module';
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {CommonModule} from "@angular/common";


@NgModule({
  declarations: [
    ResetPasswordComponent,
  ],
    imports: [
        SharedModule,
        ResetPasswordRoutingModule,
        TranslateModule,
        ReactiveFormsModule,
        FontAwesomeModule,
        CommonModule,
    ],
  exports: [
    ResetPasswordComponent
  ]
})
export class ResetPasswordModule { }
