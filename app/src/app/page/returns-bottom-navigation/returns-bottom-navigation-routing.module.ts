import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ReturnsBottomNavigationPage } from './returns-bottom-navigation.page';

const routes: Routes = [
  {
    path: '',
    component: ReturnsBottomNavigationPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ReturnsBottomNavigationPageRoutingModule {}
