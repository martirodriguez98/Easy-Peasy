import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {VerificationPageComponent} from "./verification-page/verification-page.component";
import {VerificationNeededComponent} from "./verification-needed/verification-needed.component";
import {VerificationResentComponent} from "./verification-resent/verification-resent.component";
import {VerifiedGuard} from "../guards/verified.guard";

const routes: Routes = [
  {
    path: '',
    component: VerificationPageComponent
  },
  {
    path: 'notVerified',
    component: VerificationNeededComponent,
    canActivate: [VerifiedGuard]
  },
  {
    path: 'resent',
    component: VerificationResentComponent,
    canActivate: [VerifiedGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VerificationRoutingModule { }
