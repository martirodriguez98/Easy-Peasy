import { NgModule } from '@angular/core';
import { LayoutComponent } from './layout.component';
import { LayoutRoutingModule } from './layout-routing.module'
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    LayoutComponent
  ],
  imports: [
    SharedModule,
    RouterModule,
    LayoutRoutingModule
  ],
  exports: [
    LayoutComponent
  ]
})
export class LayoutModule { }
