import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AdminPanelComponent} from "./admin-panel/admin-panel.component";
import {AdminGuard} from "../authentication/guards/admin.guard";

const routes: Routes = [
  {
    path: '',
    component: AdminPanelComponent,
    canActivate: [AdminGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
