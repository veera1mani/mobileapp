import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ReturnDetailsPage } from './return-details.page';

const routes: Routes = [
  {
    path: '',
    component: ReturnDetailsPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ReturnDetailsPageRoutingModule {}
