import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {  } from '../page/home-bottom-navi/home-bottom-navi.module';

const routes: Routes = [
  // {
  //   path:'',
  //   component:LoginComponent
  // },
  // {
  //   path:'LOgin',
  //   component:LoginComponent
  // },
  // {
  //   path:'Set_Pin',
  //   component:SetPinComponent
  // },
  // {
  //   path:'ROle',
  //   component:RolePage
  // },
  // {
  //   path: '',
  //   loadChildren: () => import('./page/common-dashboard/common-dashboard.module').then(m => m.CommonDashboardPageModule)
  // },
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthenticationRoutingModule { }
