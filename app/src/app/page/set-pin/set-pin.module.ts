import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { SetPinPageRoutingModule } from './set-pin-routing.module';

import { SetPinPage } from './set-pin.page';
import { CodeInputModule } from 'angular-code-input';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SetPinPageRoutingModule,
    ReactiveFormsModule,
    CodeInputModule
  ],
  declarations: [SetPinPage]
})
export class SetPinPageModule {}
