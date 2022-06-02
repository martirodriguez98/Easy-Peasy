import { NgModule } from '@angular/core';
import {Error404RoutingModule} from "./error-404-routing.module";
import {Error404Component} from "./error-404.component";
import {SharedModule} from "../../shared/shared.module";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";


@NgModule({
  declarations: [
    Error404Component
  ],
  imports: [
    Error404RoutingModule,
    FontAwesomeModule,
    SharedModule
  ]
})
export class Error404Module { }
