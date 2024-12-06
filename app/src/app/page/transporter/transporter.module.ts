import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { TransporterPageRoutingModule } from './transporter-routing.module';

import { TransporterPage } from './transporter.page';
import { SharedModule } from 'src/app/shared/shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SharedModule,
    TransporterPageRoutingModule
  ],
  declarations: [TransporterPage]
})
export class TransporterPageModule {}
