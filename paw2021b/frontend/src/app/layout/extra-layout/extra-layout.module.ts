import { NgModule } from '@angular/core';
import { ExtraLayoutComponent } from './extra-layout.component';
import { ExtraLayoutRoutingModule } from './extra-layout-routing.module'
import { SharedModule } from '../../shared/shared.module';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    ExtraLayoutComponent
  ],
  imports: [
    SharedModule,
    RouterModule,
    ExtraLayoutRoutingModule
  ],
  exports: [
    ExtraLayoutComponent
  ]
})
export class ExtraLayoutModule { }
