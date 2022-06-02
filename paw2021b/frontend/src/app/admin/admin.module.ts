import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import {TranslateModule} from "@ngx-translate/core";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import { PanelAdminPaginationComponent } from './panel-admin-pagination/panel-admin-pagination.component';
import { PanelBannedPaginationComponent } from './panel-banned-pagination/panel-banned-pagination.component';
import {SharedModule} from "../shared/shared.module";
import { ReportsComponent } from './reports/reports.component';

@NgModule({
  declarations: [
    AdminPanelComponent,
    PanelAdminPaginationComponent,
    PanelBannedPaginationComponent,
    ReportsComponent
  ],
    imports: [
        CommonModule,
        TranslateModule,
        FontAwesomeModule,
        AdminRoutingModule,
        SharedModule
    ]
})
export class AdminModule { }
