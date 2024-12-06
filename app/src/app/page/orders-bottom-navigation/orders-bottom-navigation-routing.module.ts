import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { OrdersBottomNavigationPage } from './orders-bottom-navigation.page';

const routes: Routes = [
  {
    path: '',
    component: OrdersBottomNavigationPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OrdersBottomNavigationPageRoutingModule {}
