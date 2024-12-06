import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { OwnerDispatchPage } from './owner-dispatch.page';

const routes: Routes = [
  {
    path: '',
    component: OwnerDispatchPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OwnerDispatchPageRoutingModule {}
