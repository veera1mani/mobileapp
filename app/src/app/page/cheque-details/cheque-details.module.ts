import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ChequeDetailsPageRoutingModule } from './cheque-details-routing.module';

import { ChequeDetailsPage } from './cheque-details.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ChequeDetailsPageRoutingModule
  ],
  declarations: [ChequeDetailsPage]
})
export class ChequeDetailsPageModule {}
