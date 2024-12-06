import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-common-dashboard',
  templateUrl: './common-dashboard.page.html',
  styleUrls: ['./common-dashboard.page.scss'],
})
export class CommonDashboardPage implements OnInit {
  selectedTab: string = 'home';
  userRole: any;
  user: any;
  
  constructor() {  
    
   }

  ngOnInit() {

    this.userRole = localStorage.getItem("user_role");
    let u : any =  localStorage.getItem('etraze_user');
    this.user = JSON.parse(u);    
    if(this.userRole === 'SUPERADMIN'){
      this.selectedTab = 'o-orders';
    }else{
      this.selectedTab = 'home';
    }   

  }

    onTabChange(tab: string) {
    this.selectedTab = tab;
  }

}
