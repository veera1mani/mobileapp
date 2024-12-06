import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlertController } from '@ionic/angular';
import * as Highcharts from 'highcharts';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-tickets-bottom-navigation',
  templateUrl: './tickets-bottom-navigation.page.html',
  styleUrls: ['./tickets-bottom-navigation.page.scss'],
})
export class TicketsBottomNavigationPage implements OnInit {

  ticketList: any;
  userRole: any;
  selectedStatus = ''
  filteredOrderList: any
  constructor(private auth: AuthService) {
   // this.filteredOrderList = this.ticketList.slice().reverse();
  }

  ngOnInit() {
    this.userRole = localStorage.getItem("user_role")
    console.log('init');
    this.getTickets();

  }


  /**
   * 
   * @param date 
   */
  calculateDays(date:any){
    // let newDate = date;
    // console.log(newDate);
    // let currentDate = new Date();
    // console.log(currentDate);

    return 2;

  }

  onSearch(event: any) {
    const searchTerm = event.target.value.toLowerCase();
    if (!searchTerm.trim()) {
      this.filteredOrderList = this.ticketList.slice().reverse();
    } else {
      //this.filteredOrderList = this.ticketList.filter(order => this.selectedStatus == order.status && order.invoiceNo.includes(searchTerm));
    }
  }

  filterOrders(status: string) {
    this.selectedStatus = status;
    if (status === 'All') {
      this.filteredOrderList = this.ticketList.slice().reverse();
    } else {
      //this.filteredOrderList = this.ticketList.filter(order => order.status === status);
    }
  }

  getTickets() {
    this.auth.openSpinner();
    this.auth.queryService('tickets').then((data) => {
      this.auth.closeSpinner(); 
      this.ticketList = data;
      console.log(data);
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    })
  }

}
