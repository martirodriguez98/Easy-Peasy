import { NgModule } from '@angular/core';
import {SessionProfileComponent} from "./profile/session-profile/session-profile.component";
import {RouterModule, Routes} from "@angular/router";
import {UserProfileComponent} from "./profile/user-profile/user-profile.component";
import {AuthGuard} from "../authentication/guards/auth.guard";
import {ProfileGuard} from "../authentication/guards/profile.guard";

const routes: Routes = [
  {
    path: ':id',
    component: UserProfileComponent,
    canActivate: [ProfileGuard]
  },
  {
    path: '',
    component: SessionProfileComponent
  }
]

@NgModule({
  declarations: [],
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
