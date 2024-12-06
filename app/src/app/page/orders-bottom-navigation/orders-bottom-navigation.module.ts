import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { OrdersBottomNavigationPageRoutingModule } from './orders-bottom-navigation-routing.module';

import { OrdersBottomNavigationPage } from './orders-bottom-navigation.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    OrdersBottomNavigationPageRoutingModule
  ],
  declarations: [OrdersBottomNavigationPage]
})
export class OrdersBottomNavigationPageModule {}
