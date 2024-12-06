import { NgModule,CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import { AuthService } from './services/auth.service';


const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./page/splash-screen/splash-screen.module').then( m => m.SplashScreenPageModule)
  },  
  {
    path: 'login-page',
    loadChildren: () => import('./page/login/login.module').then( m => m.LoginPageModule)
  },
  {
    path: 'common-dashboard',
    loadChildren: () => import('./page/common-dashboard/common-dashboard.module').then(m => m.CommonDashboardPageModule)
  },
  {
    path: 'Authentication',
    loadChildren: () => import('./Authentication/authentication.module').then(m => m.AuthenticationModule)
  },
  {
    path: 'order-details',
    loadChildren: () => import('./page/order-details/order-details.module').then( m => m.OrderDetailsPageModule)
  },
  {
    path: 'transporter',
    loadChildren: () => import('./page/transporter/transporter.module').then( m => m.TransporterPageModule)
  },
  {
    path: 'transport-order',
    loadChildren: () => import('./page/transport-order/transport-order.module').then( m => m.TransportOrderPageModule)
  },
  {
    path: 'owner-dashboard',
    loadChildren: () => import('./page/owner-dashboard/owner-dashboard.module').then( m => m.OwnerDashboardPageModule)
  },  
  {
    path: 'set-pin',
    loadChildren: () => import('./page/set-pin/set-pin.module').then( m => m.SetPinPageModule)
  },
  {
    path: 'role/:ticketId',
    loadChildren: () => import('./page/role/role.module').then( m => m.RolePageModule)
  },
  {
    path: 'etraze-notification',
    loadChildren: () => import('./page/etraze-notification/etraze-notification.module').then( m => m.EtrazeNotificationPageModule)
  },
  {
    path: 'return-details',
    loadChildren: () => import('./page/return-details/return-details.module').then( m => m.ReturnDetailsPageModule)
  },
  {
    path: 'ticket-details',
    loadChildren: () => import('./page/ticket-details/ticket-details.module').then( m => m.TicketDetailsPageModule)
  },
  {
    path: 'cheque-details',
    loadChildren: () => import('./page/cheque-details/cheque-details.module').then( m => m.ChequeDetailsPageModule)
  },
  {
    path: 'user-order',
    loadChildren: () => import('./page/user-order/user-order.module').then( m => m.UserOrderPageModule)
  },
  {
    path: 'user-return',
    loadChildren: () => import('./page/user-return/user-return.module').then( m => m.UserReturnPageModule)
  },
  {
    path: 'enter-pin',
    loadChildren: () => import('./page/enter-pin/enter-pin.module').then( m => m.EnterPinPageModule)
  },
  {
    path: 'invoice-details',
    loadChildren: () => import('./page/invoice-details/invoice-details.module').then( m => m.InvoiceDetailsPageModule)
  },
  {
    path: 'invoice-checking',
    loadChildren: () => import('./page/invoice-checking/invoice-checking.module').then( m => m.InvoiceCheckingPageModule)
  },   
  {
    path: 'visitor',
    loadChildren: () => import('./page/visitor/visitor.module').then( m => m.VisitorPageModule)
  },
  {
    path: 'register',
    loadChildren: () => import('./page/register/register.module').then( m => m.RegisterPageModule)
  },  {
    path: 'scan-qr',
    loadChildren: () => import('./page/scan-qr/scan-qr.module').then( m => m.ScanQrPageModule)
  },
  {
    path: 'common-page',
    loadChildren: () => import('./page/common-page/common-page.module').then( m => m.CommonPagePageModule)
  },




];
@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers:[AuthService]
})
export class AppRoutingModule {}
