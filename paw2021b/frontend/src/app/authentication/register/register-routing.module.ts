import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register.component';
import {UnauthGuard} from "../guards/unauth.guard";

const routes: Routes = [
  {
    path: '',
    component: RegisterComponent,
    canActivate: [UnauthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RegisterRoutingModule { }
