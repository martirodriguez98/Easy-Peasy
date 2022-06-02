import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import("./layout/layout.module").then(m => m.LayoutModule)
  },
  {
    path: '',
    loadChildren: () =>
      import("./layout/extra-layout/extra-layout.module").then(m => m.ExtraLayoutModule)
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
