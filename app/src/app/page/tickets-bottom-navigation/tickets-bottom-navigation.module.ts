import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HighchartsChartModule } from 'highcharts-angular';

import { IonicModule } from '@ionic/angular';

import { TicketsBottomNavigationPageRoutingModule } from './tickets-bottom-navigation-routing.module';

import { TicketsBottomNavigationPage } from './tickets-bottom-navigation.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    TicketsBottomNavigationPageRoutingModule,
    HighchartsChartModule,
    
  ],
  declarations: [TicketsBottomNavigationPage]
})
export class TicketsBottomNavigationPageModule {}
