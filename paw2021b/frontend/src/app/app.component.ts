import { Component } from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontend';
  constructor(
    public translateService: TranslateService
  ){
  }

  ngOnInit(): void {
    this.translateService.addLangs(['en','es']);
    this.translateService.setDefaultLang('en');
    this.translateService.use(<string>this.translateService.getBrowserLang());

  }

}
