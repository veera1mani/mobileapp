import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { InvoiceCheckingPageRoutingModule } from './invoice-checking-routing.module';

import { InvoiceCheckingPage } from './invoice-checking.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    InvoiceCheckingPageRoutingModule
  ],
  declarations: [InvoiceCheckingPage]
})
export class InvoiceCheckingPageModule {}
