import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {UserProfileComponent} from "./user-profile.component";
import {SharedModule} from "../../../shared/shared.module";
import {UserRoutingModule} from "../../user-routing.module";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {ReactiveFormsModule} from "@angular/forms";



@NgModule({
  declarations: [
    UserProfileComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    UserRoutingModule,
    FontAwesomeModule,
    ReactiveFormsModule
  ],
  exports: [
    UserProfileComponent
  ]
})
export class UserProfileModule { }
