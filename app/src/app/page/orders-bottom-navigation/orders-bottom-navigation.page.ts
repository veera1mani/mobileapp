import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-orders-bottom-navigation',
  templateUrl: './orders-bottom-navigation.page.html',
  styleUrls: ['./orders-bottom-navigation.page.scss'],
})
export class OrdersBottomNavigationPage implements OnInit {
  //Delivered Cancelled Shipped
  orderList = [
    {
      refNo: "AAS1O2HA3TY",
      custName: 'Office of the dean, Government medical',
      emailDate: '20-04-2023 12:08 PM',
      status: 'Delivered',
      invoiceNo: '783738473488777',
      item: '5'
    },
    {
      refNo: "XYS987PQR321",
      custName: 'General Hospital, City Health',
      emailDate: '21-04-2023 01:30 PM',
      status: 'Cancelled',
      invoiceNo: '9876543210',
      item: '8'
    },
    {
      refNo: "LMN456XYZ789",
      custName: 'Medical Supplies Inc.',
      emailDate: '22-04-2023 02:45 PM',
      status: 'Shipped',
      invoiceNo: '1122334455',
      item: '12'
    },
    {
      refNo: "ABC123XYZ456",
      custName: 'City Medical Center',
      emailDate: '23-04-2023 03:20 PM',
      status: 'Shipped',
      invoiceNo: '5432109876',
      item: '10'
    },
    {
      refNo: "PQR789LMN321",
      custName: 'Health Solutions Ltd.',
      emailDate: '24-04-2023 04:55 PM',
      status: 'Cancelled',
      invoiceNo: '9998887776',
      item: '15'
    },
    {
      refNo: "JKL555MNO888",
      custName: 'Community Clinic Services',
      emailDate: '25-04-2023 05:30 PM',
      status: 'Cancelled',
      invoiceNo: '4445556667',
      item: '7'
    },
    {
      refNo: "DEF999GHI111",
      custName: 'Wellness Supplies Co.',
      emailDate: '26-04-2023 06:15 PM',
      status: 'Delivered',
      invoiceNo: '8765432109',
      item: '20'
    },
    {
      refNo: "QRS333TUV777",
      custName: 'Emergency Medical Solutions',
      emailDate: '27-04-2023 07:00 PM',
      status: 'Shipped',
      invoiceNo: '1234567890',
      item: '18'
    },
    {
      refNo: "XYZ777ABC111",
      custName: 'County Health Services',
      emailDate: '28-04-2023 08:25 PM',
      status: 'Cancelled',
      invoiceNo: '5556667778',
      item: '25'
    },
    {
      refNo: "MNO222PQR888",
      custName: 'MedTech Supplies Corp.',
      emailDate: '29-04-2023 09:10 PM',
      status: 'Cancelled',
      invoiceNo: '9876543211',
      item: '22'
    }
  ];
  selectedStatus = ''
  filteredOrderList: any
  constructor(private auth:AuthService) {
    this.filteredOrderList = this.orderList.slice().reverse();
  }

  ngOnInit() {
    console.log('init');
    this.getOrders();
  }

  onSearch(event: any) {
    const searchTerm = event.target.value.toLowerCase();
    if (!searchTerm.trim()) {
      this.filteredOrderList = this.orderList.slice().reverse();
    } else {
      this.filteredOrderList = this.orderList.filter(order => this.selectedStatus == order.status && order.invoiceNo.includes(searchTerm));
    }
  }

  filterOrders(status: string) {
    this.selectedStatus = status;
    if (status === 'All') {
      this.filteredOrderList = this.orderList.slice().reverse();
    } else {
      this.filteredOrderList = this.orderList.filter(order => order.status === status);
    }
  }

  //api call for get orders
  getOrders(){
    this.auth.openSpinner();
    this.auth.queryService('orders').then((data) => {
      this.auth.closeSpinner();
      console.log(data);
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    })
  }









}
