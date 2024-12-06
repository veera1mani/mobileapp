import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ReturnDetailsPageRoutingModule } from './return-details-routing.module';

import { ReturnDetailsPage } from './return-details.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ReturnDetailsPageRoutingModule
  ],
  declarations: [ReturnDetailsPage]
})
export class ReturnDetailsPageModule {}
