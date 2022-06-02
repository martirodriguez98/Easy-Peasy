import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NewRecipeComponent } from './new-recipe/new-recipe.component';
import {RecipeComponent} from "./recipe.component";
import {SharedModule} from "../shared/shared.module";
import {RecipeRoutingModule} from "./recipe-routing.module";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {ReactiveFormsModule} from "@angular/forms";
import { CommentComponent } from './comment/comment.component';
import {ShareComponent} from "./share/share.component";
import {RouterModule} from "@angular/router";



@NgModule({
  declarations: [
    RecipeComponent,
    NewRecipeComponent,
    CommentComponent,
    ShareComponent
  ],
  imports: [
    SharedModule,
    RecipeRoutingModule,
    FontAwesomeModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule
  ],
  exports: [
    RecipeComponent
  ]
})
export class RecipeModule { }
