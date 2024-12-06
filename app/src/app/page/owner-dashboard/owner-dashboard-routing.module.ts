import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { OwnerDashboardPage } from './owner-dashboard.page';

const routes: Routes = [
  {
    path: '',
    component: OwnerDashboardPage,
    children: [      
  {
    path: 'orders',
    loadChildren: () => import('../../page/owner-orders/owner-orders.module').then( m => m.OwnerOrdersPageModule)
  },
  {
    path: 'tickets',
    loadChildren: () => import('../../page/owner-tickets/owner-tickets.module').then( m => m.OwnerTicketsPageModule)
  },
  {
    path: 'dispatch',
    loadChildren: () => import('../../page/owner-dispatch/owner-dispatch.module').then( m => m.OwnerDispatchPageModule)
  },
      {
        path: '',
        redirectTo: '/orders',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '',
    redirectTo: '/owner-dashboard/orders',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OwnerDashboardPageRoutingModule {}
