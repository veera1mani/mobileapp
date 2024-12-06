import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { EnterPinPageRoutingModule } from './enter-pin-routing.module';

import { EnterPinPage } from './enter-pin.page';
import { CodeInputModule } from 'angular-code-input';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    EnterPinPageRoutingModule,
    CodeInputModule
  ],
  declarations: [EnterPinPage]
})
export class EnterPinPageModule {}
