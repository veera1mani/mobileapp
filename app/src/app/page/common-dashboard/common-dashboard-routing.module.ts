import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CommonDashboardPage } from './common-dashboard.page';

const routes: Routes = [
  {
    path: '',
    component: CommonDashboardPage,
    children: [
      {
        path: 'home',
        loadChildren: () => import('../home-bottom-navi/home-bottom-navi.module').then( m => m.HomeBottomNaviPageModule)
      },
      {
        path: 'orders-bottom-navi',
        loadChildren: () => import('../orders-bottom-navigation/orders-bottom-navigation.module').then( m => m.OrdersBottomNavigationPageModule)
      },
      {
        path: 'tickets',  
        loadChildren: () => import('../../page/tickets/tickets.module').then( m => m.TicketsPageModule)
      },
      {
        path: 'orders',
        loadChildren: () => import( '../../page/orders/orders.module').then( m => m.OrdersPageModule)      
      },
      {
        path: 'returns',
        loadChildren: () => import('../../page/returns/returns.module').then( m => m.ReturnsPageModule)
      },
      {
        path: 'cheques',
        loadChildren: () => import('../../page/cheques/cheques.module').then( m => m.ChequesPageModule)
      },
      {
        path: 'user-home',
        loadChildren: () => import('../../page/user-home/user-home.module').then( m => m.UserHomePageModule)
      },
      {
        path: 'o-orders',
        loadChildren: () => import('../../page/owner-orders/owner-orders.module').then( m => m.OwnerOrdersPageModule)
      },
      {
        path: 'o-tickets',
        loadChildren: () => import('../../page/owner-tickets/owner-tickets.module').then( m => m.OwnerTicketsPageModule)
      },
      {
        path: 'o-dispatch',
        loadChildren: () => import('../../page/owner-dispatch/owner-dispatch.module').then( m => m.OwnerDispatchPageModule)
      },      
      {
        path: 'transporter',
        loadChildren: () => import('../../page/transporter/transporter.module').then( m => m.TransporterPageModule)
      },
      {
        path: '',
        redirectTo: '/home',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '',
    redirectTo: '/common-dashboard/home',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CommonDashboardPageRoutingModule {}
