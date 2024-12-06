import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ChequesPageRoutingModule } from './cheques-routing.module';

import { ChequesPage } from './cheques.page';
import { SharedModule } from 'src/app/shared/shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SharedModule,
    ChequesPageRoutingModule
  ],
  declarations: [ChequesPage]
})
export class ChequesPageModule {}
