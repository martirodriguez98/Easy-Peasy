import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchComponent } from './search.component';
import {SearchRoutingModule} from "./search-routing.module";
import {TranslateModule} from "@ngx-translate/core";
import {SharedModule} from "../shared/shared.module";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    SearchComponent
  ],
  imports: [
    CommonModule,
    SearchRoutingModule,
    TranslateModule,
    SharedModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    FormsModule
  ],
  exports: [
    SearchComponent,

  ]
})
export class SearchModule { }
