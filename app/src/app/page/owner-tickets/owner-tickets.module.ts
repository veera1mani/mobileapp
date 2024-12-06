import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { OwnerTicketsPageRoutingModule } from './owner-tickets-routing.module';

import { OwnerTicketsPage } from './owner-tickets.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    OwnerTicketsPageRoutingModule
  ],
  declarations: [OwnerTicketsPage]
})
export class OwnerTicketsPageModule {}
