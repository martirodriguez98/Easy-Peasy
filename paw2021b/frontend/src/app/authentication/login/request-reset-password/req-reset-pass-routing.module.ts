import {RouterModule, Routes} from "@angular/router";
import {ReqResetPassComponent} from "./req-reset-pass.component";
import {NgModule} from "@angular/core";
import {UnauthGuard} from "../../guards/unauth.guard";


const routes: Routes = [
    {
      path: '',
      component: ReqResetPassComponent,
      canActivate: [UnauthGuard]
    }
  ]

  @NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class ReqResetPassRoutingModule {
  }
