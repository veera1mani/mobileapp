import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ReturnsBottomNavigationPageRoutingModule } from './returns-bottom-navigation-routing.module';

import { ReturnsBottomNavigationPage } from './returns-bottom-navigation.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ReturnsBottomNavigationPageRoutingModule
  ],
  declarations: [ReturnsBottomNavigationPage]
})
export class ReturnsBottomNavigationPageModule {}
