import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {BannedComponent} from "./banned.component";
import {NotBannedGuard} from "../guards/not-banned.guard";

const routes: Routes = [
  {
      path: '',
      component: BannedComponent,
      canActivate: [NotBannedGuard],
    }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BannedRoutingModule { }
