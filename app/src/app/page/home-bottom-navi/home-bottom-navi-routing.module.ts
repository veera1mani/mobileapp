import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeBottomNaviPage } from './home-bottom-navi.page';
import { NotificationPage } from './notification/notification.page';

export const routes: Routes = [
  {
    path: '',
    component: HomeBottomNaviPage
  },
  {
    path: 'Home',
    component: HomeBottomNaviPage
  },
  {
    path: 'Notification',
    component: NotificationPage
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HomeBottomNaviPageRoutingModule {}
