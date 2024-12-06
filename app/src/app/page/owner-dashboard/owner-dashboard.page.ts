import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-owner-dashboard',
  templateUrl: './owner-dashboard.page.html',
  styleUrls: ['./owner-dashboard.page.scss'],
})
export class OwnerDashboardPage implements OnInit {

  selectedTab: string = 'orders';

  constructor() { }

  ngOnInit() {
    console.log('init');


  }
  onTabChange(tab: string) {
    this.selectedTab = tab;
    
  }

}
