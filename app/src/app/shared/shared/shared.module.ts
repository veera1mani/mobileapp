import {CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA} from '@angular/core';
import {CommonModule} from '@angular/common';
import {IonicModule} from '@ionic/angular';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { DashboardHeaderComponent } from '../../shared/dashboard-header/dashboard-header.component';
import { TypeaheadComponent } from '../typeahead/typeahead.component';
 
    // 

@NgModule({
    declarations: [DashboardHeaderComponent,TypeaheadComponent],
  imports: [
    CommonModule,
    IonicModule,
    ReactiveFormsModule,
    FormsModule,
  ],
  exports:[DashboardHeaderComponent,TypeaheadComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
    // exports: [ PipeModule],
})
export class SharedModule { }