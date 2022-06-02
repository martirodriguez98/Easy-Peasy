import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RegisterRoutingModule } from './register-routing.module';
import { RegisterComponent } from './register.component';
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {SharedModule} from "../../shared/shared.module";


@NgModule({
  declarations: [
    RegisterComponent
  ],
  imports: [
    CommonModule,
    RegisterRoutingModule,
    TranslateModule,
    ReactiveFormsModule,
    SharedModule
  ],
  exports: [
    RegisterComponent
  ]
})
export class RegisterModule { }
