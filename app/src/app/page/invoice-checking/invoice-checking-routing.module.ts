import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { InvoiceCheckingPage } from './invoice-checking.page';

const routes: Routes = [
  {
    path: '',
    component: InvoiceCheckingPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class InvoiceCheckingPageRoutingModule {}
