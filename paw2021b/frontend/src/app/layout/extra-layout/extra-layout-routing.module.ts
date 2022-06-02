import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {ExtraLayoutComponent} from "./extra-layout.component";
import {StartGuard} from "../../authentication/guards/start.guard";

const routes: Routes = [
  {
    path: '',
    component: ExtraLayoutComponent,
    canActivate: [StartGuard],
    children: [
      {
        path: '404',
        loadChildren: () => import("../../error/error-404/error-404.module").then(m => m.Error404Module),
      },
      {
        path:'404',
        loadChildren: () => import("../../error/error-404/error-404.module").then(m => m.Error404Module),
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
export class ExtraLayoutRoutingModule {
}
