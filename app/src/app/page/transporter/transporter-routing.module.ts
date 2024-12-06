import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { TransporterPage } from './transporter.page';

const routes: Routes = [
  {
    path: '',
    component: TransporterPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TransporterPageRoutingModule {}
