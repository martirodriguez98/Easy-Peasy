import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {LayoutComponent} from "./layout.component";
import {LandingComponent} from '../landing/landing.component';
import {StartGuard} from "../authentication/guards/start.guard";
import {UnauthGuard} from "../authentication/guards/unauth.guard";

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    canActivate: [StartGuard],
    children: [
      {
        path: '',
        component: LandingComponent,
        loadChildren: () => import("../landing/landing.module").then(m => m.LandingModule)
      },
      {
        path: 'login',
        canActivate: [UnauthGuard],
        loadChildren: () => import("../authentication/login/login.module").then(m => m.LoginModule)
      },
      {
        path: 'register',
        canActivate: [UnauthGuard],
        loadChildren: () => import("../authentication/register/register.module").then(m => m.RegisterModule)
      },
      {
        path: 'profile',
        loadChildren: () => import("../user/profile/session-profile/session-profile.module").then(m => m.SessionProfileModule)
      },
      {
        path: 'profile/{id}',
        loadChildren: () => import("../user/profile/user-profile/user-profile.module").then(m => m.UserProfileModule)
      },
      {
        path: 'recipes',
        loadChildren: () => import("../recipe/recipe.module").then(m => m.RecipeModule)
      },
      {
        path: 'search',
        loadChildren: () => import("../search/search.module").then(m => m.SearchModule)
      },
      {
        path: 'adminPanel',
        loadChildren: () => import("../admin/admin.module").then(m => m.AdminModule)
      },
      {
        path:'verification',
        loadChildren: () => import("../authentication/verification/verification.module").then(m => m.VerificationModule)
      },
      {
        path:'passwordReset',
        loadChildren: () => import("../authentication/reset-password/reset-password.module").then(m => m.ResetPasswordModule)
      },
      {
        path:'banned',
        loadChildren: () => import("../authentication/banned/banned.module").then(m=> m.BannedModule)
      },
      {
        path:'**',
        loadChildren: () => import("../error/error-404/error-404.module").then(m => m.Error404Module),
        data: {
          code: 404,
        }
      }

    ]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LayoutRoutingModule {
}
