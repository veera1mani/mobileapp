import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { EtrazeNotificationPageRoutingModule } from './etraze-notification-routing.module';

import { EtrazeNotificationPage } from './etraze-notification.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    EtrazeNotificationPageRoutingModule
  ],
  declarations: [EtrazeNotificationPage]
})
export class EtrazeNotificationPageModule {}
