import {RouterModule, Routes} from "@angular/router";
import {ShareComponent} from "./share.component";
import {NgModule} from "@angular/core";
import {UnauthGuard} from "../../authentication/guards/unauth.guard";


const routes: Routes = [
    {
      path: '',
      component: ShareComponent,
      canActivate: [UnauthGuard]
    }
  ]

  @NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class ShareRoutingModule {
  }
