import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { CommonDashboardPage } from 'src/app/page/common-dashboard/common-dashboard.page';
import { Subscription } from 'rxjs';
import { Platform } from '@ionic/angular';


@Component({
  selector: 'app-orders',
  templateUrl: './orders.page.html',
  styleUrls: ['./orders.page.scss'],
})
export class OrdersPage implements OnInit, OnDestroy {

  orderList: any;
  order: any
  backButtonSubscription: Subscription = new Subscription;
  selectedStatus = ''
  filteredOrderList: any;
  userRole: any ;
  dataFetched: boolean = false;
  user: any;
  users: any;


  constructor(public dashboard: CommonDashboardPage, private platform: Platform ,  private auth:AuthService, private router: Router) {
  }

  ngOnInit() {
    this.userRole= localStorage.getItem('user_role');
    this.user= localStorage.getItem('etraze_user');
    let u = JSON.parse(this.user);
    this.users = u;    
    this.queryService('INVOICE CREATED');
  }

  handleRefresh(event : any) {
    setTimeout(() => {
      this.queryService('INVOICE CREATED');
      event.target.complete();
    }, 1500);
  }
  
  ionViewWillEnter(){
    this.backButtonSubscription = this.platform.backButton.subscribeWithPriority(100, () => {      
      console.log('backbutton 5 ');
      if(this.users.roleName == 'TRANSPORT'){
        this.router.navigateByUrl('/common-dashboard/transporter');
        this.dashboard.onTabChange('home');
      } else if(this.users.roleName == 'USER'){
        this.router.navigateByUrl('/common-dashboard/user-home');
        this.dashboard.onTabChange('home');
      } else {
        this.router.navigateByUrl('/common-dashboard/home');
        this.dashboard.onTabChange('home');
      }
    });
  }
  ionViewWillLeave(){
    this.backButtonSubscription.unsubscribe();
  }
  ngOnDestroy(): void {
    console.log('destroy');
  }

  onSearch(event: any) {
    const searchTerm = event.target.value.toLowerCase();
    if (!searchTerm.trim()) {
      this.filteredOrderList = this.orderList.slice();
    } else {
      this.filteredOrderList = this.orderList.filter((d: any) => d.invoiceNumber.includes(searchTerm) > 0 );      
    }
  }

  filterOrders(status: string) {
    this.selectedStatus = status; 
    if (status === 'All') {
      this.filteredOrderList = this.orderList.slice();
    } else {
      this.filteredOrderList = this.orderList.filter((order: { status: string; }) => order.status === status);
    }
  } 

  
//orders
//app-orderss
//'orders-app?tenantId='+this.users.tenantId+'&userId='+ this.users.userId
// tenant-transporter-orders -- transport
  getOrders(){
    this.auth.openSpinner();
    // if(this.users.roleName == 'TRANSPORT'){
    //   this.queryService('tenant-transporter-orders');
    // } else {
    //   this.queryService('orders-app?tenantId='+this.users.tenantId+'&userId='+ this.users.userId);
    // }
    // this.queryService('order-mobile');
  }

  queryService(sts: string){
    this.auth.openSpinner();
    this.auth.queryService('order-mobile?status='+ sts).then((data) => {
      this.auth.closeSpinner();
      this.orderList = data;
      if(this.orderList){
        this.dataFetched = true;
      }
      this.filteredOrderList = this.orderList.slice().reverse();
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    })
  }
  backNav(){
    if(this.userRole == 'MANAGER'){
      this.router.navigateByUrl('/common-dashboard/home');
      this.dashboard.onTabChange('home');
    }else if(this.userRole == 'USER'){
      this.router.navigateByUrl('/common-dashboard/user-home');
      this.dashboard.onTabChange('home');
    }else {
      this.router.navigateByUrl('/common-dashboard/transporter');
      this.dashboard.onTabChange('home');
    }
  }


  ticketDetail(ticketId: any , inv: any){
    // this.router.navigate(['/order-details' , { ticketNo: ticketId }]); 
    // console.log('klndaklvna', ticketId); 
    this.router.navigate(['/order-details'] , {
      queryParams: {
        ticketNo: ticketId,
        invoiceNo: inv , 
      }
    });  
  }
}
