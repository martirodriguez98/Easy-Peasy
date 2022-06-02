import { NgModule } from '@angular/core';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {WebpackTranslateLoader} from "./webpack-translate-loader";

@NgModule({
  declarations: [],
  imports: [
    TranslateModule.forRoot({
      defaultLanguage: 'en',
      loader: {
        provide: TranslateLoader,
        useClass: WebpackTranslateLoader
      }
    })
  ],
  exports: [TranslateModule]
})
export class AppTranslateModule { }
