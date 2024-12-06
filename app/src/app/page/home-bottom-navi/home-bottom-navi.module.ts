import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HighchartsChartModule } from 'highcharts-angular';
import { NotificationPage } from './notification/notification.page';

import { IonicModule } from '@ionic/angular';
import { DashboardHeaderComponent } from '../../shared/dashboard-header/dashboard-header.component';
import { HomeBottomNaviPageRoutingModule } from './home-bottom-navi-routing.module';

import { HomeBottomNaviPage } from './home-bottom-navi.page';
import { SharedModule } from 'src/app/shared/shared/shared.module';

@NgModule({
    declarations: [HomeBottomNaviPage, NotificationPage],
    imports: [
        CommonModule,
        FormsModule,SharedModule,
        IonicModule,
        HomeBottomNaviPageRoutingModule,
        HighchartsChartModule,
        
    ]
})
export class HomeBottomNaviPageModule {}
