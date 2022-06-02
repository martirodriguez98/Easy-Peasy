import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {Error404Component} from "./error-404.component";

const routes: Routes = [
  {
    path: '',
    component: Error404Component
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})

export class Error404RoutingModule{
}
