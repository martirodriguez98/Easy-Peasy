import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BannedRoutingModule } from './banned-routing.module';
import { BannedComponent } from './banned.component';
import {TranslateModule} from "@ngx-translate/core";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";


@NgModule({
  declarations: [
    BannedComponent
  ],
  imports: [
    CommonModule,
    BannedRoutingModule,
    TranslateModule,
    FontAwesomeModule
  ]
})
export class BannedModule { }
