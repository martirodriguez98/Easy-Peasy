import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { FooterComponent } from './footer/footer.component';
import { RouterModule } from '@angular/router';
import { RecipeCardComponent } from './recipe-card/recipe-card.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {TranslateModule} from "@ngx-translate/core";
import {FootComponent} from "./foot/foot.component";
import { UserCardComponent } from './user-card/user-card.component';
import { PaginationComponent } from './pagination/pagination.component';



@NgModule({
  declarations: [
    NavbarComponent,
    FooterComponent,
    RecipeCardComponent,
    FootComponent,
    PaginationComponent,
    UserCardComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    FontAwesomeModule,
    TranslateModule
  ],
    exports: [
        NavbarComponent,
        FooterComponent,
        RecipeCardComponent,
        TranslateModule,
        FootComponent,
        UserCardComponent,
        PaginationComponent
    ]


})
export class SharedModule { }
