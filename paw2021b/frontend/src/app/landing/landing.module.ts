import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LandingComponent } from './landing.component';
import {SharedModule} from "../shared/shared.module";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {RouterModule} from "@angular/router";



@NgModule({
  declarations: [
    LandingComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    FontAwesomeModule
  ]
})
export class LandingModule { }
