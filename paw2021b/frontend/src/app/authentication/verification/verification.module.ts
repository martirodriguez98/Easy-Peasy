import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { VerificationRoutingModule } from './verification-routing.module';
import { VerificationPageComponent } from './verification-page/verification-page.component';
import {TranslateModule} from "@ngx-translate/core";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import { VerificationNeededComponent } from './verification-needed/verification-needed.component';
import { VerificationResentComponent } from './verification-resent/verification-resent.component';


@NgModule({
  declarations: [
    VerificationPageComponent,
    VerificationNeededComponent,
    VerificationResentComponent
  ],
  imports: [
    CommonModule,
    VerificationRoutingModule,
    TranslateModule,
    FontAwesomeModule
  ]
})
export class VerificationModule { }
