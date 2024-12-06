import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { UserReturnPageRoutingModule } from './user-return-routing.module';

import { UserReturnPage } from './user-return.page';
import { SharedModule } from 'src/app/shared/shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,ReactiveFormsModule,
    UserReturnPageRoutingModule,
    SharedModule,
  ],
  providers: [
    DatePipe // Provide DatePipe
  ],
  declarations: [UserReturnPage]
})
export class UserReturnPageModule {}
