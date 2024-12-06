import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { OwnerOrdersPageRoutingModule } from './owner-orders-routing.module';

import { OwnerOrdersPage } from './owner-orders.page';
import { SharedModule } from 'src/app/shared/shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,SharedModule,
    OwnerOrdersPageRoutingModule
  ],
  declarations: [OwnerOrdersPage]
})
export class OwnerOrdersPageModule {}
