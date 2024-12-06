import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ChequesPage } from './cheques.page';

const routes: Routes = [
  {
    path: '',
    component: ChequesPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ChequesPageRoutingModule {}
