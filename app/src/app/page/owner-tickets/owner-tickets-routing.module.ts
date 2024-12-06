import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { OwnerTicketsPage } from './owner-tickets.page';

const routes: Routes = [
  {
    path: '',
    component: OwnerTicketsPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OwnerTicketsPageRoutingModule {}
