import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { UserHomePageRoutingModule } from './user-home-routing.module';
import { HighchartsChartModule } from 'highcharts-angular';
import { UserHomePage } from './user-home.page';
import { SharedModule } from 'src/app/shared/shared/shared.module';

@NgModule({
    declarations: [UserHomePage],
    imports: [
        CommonModule,
        FormsModule,
        IonicModule,SharedModule,
        UserHomePageRoutingModule,
        HighchartsChartModule,                
    ]
})
export class UserHomePageModule {}
