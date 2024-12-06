import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EtrazeNotificationPage } from './etraze-notification.page';

const routes: Routes = [
  {
    path: '',
    component: EtrazeNotificationPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class EtrazeNotificationPageRoutingModule {}
