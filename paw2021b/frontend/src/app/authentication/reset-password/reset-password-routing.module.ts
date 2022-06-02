import {RouterModule, Routes} from "@angular/router";
import {ResetPasswordComponent} from "./reset-password.component";
import {NgModule} from "@angular/core";
import {UnauthGuard} from "../guards/unauth.guard";

const routes: Routes = [
    {
      path: '',
      component: ResetPasswordComponent,
      canActivate: [UnauthGuard]
    }
  ]

  @NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class ResetPasswordRoutingModule {
  }
