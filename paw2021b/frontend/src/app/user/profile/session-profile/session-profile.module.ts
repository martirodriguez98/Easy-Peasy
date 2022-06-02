import { NgModule } from '@angular/core';
import { SessionProfileComponent } from './session-profile.component';
import {SharedModule} from "../../../shared/shared.module";
import {UserRoutingModule} from "../../user-routing.module";
import {CommonModule} from "@angular/common";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {ReactiveFormsModule} from "@angular/forms";



@NgModule({
  declarations: [
    SessionProfileComponent
  ],
  imports: [
    SharedModule,
    UserRoutingModule,
    FontAwesomeModule,
    CommonModule,
    ReactiveFormsModule
  ],
  exports: [
    SessionProfileComponent
  ]
})
export class SessionProfileModule { }
