import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { CommonDashboardPageRoutingModule } from './common-dashboard-routing.module';

import { CommonDashboardPage } from './common-dashboard.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    CommonDashboardPageRoutingModule
  ],
  declarations: [CommonDashboardPage]
})
export class CommonDashboardPageModule {}
