import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { TicketsBottomNavigationPage } from './tickets-bottom-navigation.page';

const routes: Routes = [
  {
    path: '',
    component: TicketsBottomNavigationPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TicketsBottomNavigationPageRoutingModule {}
