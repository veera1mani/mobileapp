import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthenticationRoutingModule } from './authentication-routing.module';
import { FormsModule } from '@angular/forms';
import { HighchartsChartModule } from 'highcharts-angular';
import { IonicModule } from '@ionic/angular';

export class HomeBottomNaviPageModule {}

@NgModule({
  declarations: [
   
  ],
  imports: [
    CommonModule,
    AuthenticationRoutingModule,
    FormsModule,
    HighchartsChartModule,
    IonicModule
  ]
})
export class AuthenticationModule { }
