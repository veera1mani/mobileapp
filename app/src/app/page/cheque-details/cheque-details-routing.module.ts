import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ChequeDetailsPage } from './cheque-details.page';

const routes: Routes = [
  {
    path: '',
    component: ChequeDetailsPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ChequeDetailsPageRoutingModule {}
