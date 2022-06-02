import {HttpClientTestingModule} from "@angular/common/http/testing";
import {NgModule} from "@angular/core";
import {AppTranslateModule} from "./app-translate.module";
import {FontAwesomeTestingModule} from "@fortawesome/angular-fontawesome/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";

@NgModule({
  providers: [],
  exports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientTestingModule,
    RouterTestingModule,
    AppTranslateModule,
    FontAwesomeTestingModule,
  ],
})
export class TestingModule {
}
