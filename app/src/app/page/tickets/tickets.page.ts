import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { AuthService } from 'src/app/services/auth.service';
import { CommonDashboardPage } from 'src/app/page/common-dashboard/common-dashboard.page';


@Component({
  selector: 'app-tickets',
  templateUrl: './tickets.page.html',
  styleUrls: ['./tickets.page.scss'],
})
export class TicketsPage implements OnInit {

  ticketList: any;
  userRole: any;
  users:any;
  
  daysDifference: any;
  results: any;
  ticketNumber: string[] = [];

  days: any;
  dataFetched: boolean = false;

  public tickets = [...this.ticketNumber];

  selectedStatus = ''
  filteredOrderList: any;
  user: any;

  constructor(public dashboard: CommonDashboardPage, private auth: AuthService , private router: Router) {
  }

  ngOnInit() {
    this.userRole = localStorage.getItem("user_role");
    this.user = localStorage.getItem("etraze_user");
    let u = JSON.parse(this.user);
    this.users = u;
    console.log('users : ',this.users);
    this.getTickets();
  }
  
  /**
   * 
   * @param date 
   */

  calculateDays(date:any){
    let startDate = date;
    let endDate = new Date();
    const startMoment = moment(startDate, 'YYYY-MM-DD');
    const endMoment = moment(endDate, 'YYYY-MM-DD');

    if (startMoment.isValid() && endMoment.isValid()) {
      this.daysDifference = endMoment.diff(startMoment, 'days');
    } else {
      console.log(startDate , 'given date is not valid');
    }    
    return this.daysDifference;
    
  }



  onSearch(event: any) {
    const searchTerm = event.target.value.toUpperCase();
    if (!searchTerm.trim()) {
      this.filteredOrderList = this.ticketList.slice();
    } else {
      this.filteredOrderList = this.ticketList.filter((d: any) => d.ticketId.includes(searchTerm) > 0 );      
    }    
  }

  filterOrders(status: string) {
    this.selectedStatus = status;
    if (status === 'All') {
      this.filteredOrderList = this.ticketList.slice();
    } else {
      this.filteredOrderList = this.ticketList.filter((order: { status: string; }) => order.status === status);
    }
  }


  /**
   * "ticketss?" + "page=" + event + "&value=" + this.searchName+"&status="+ this.status+ "&sortBy=" + this.sortingBy + "&sortDir=" + this.sortingDir
   */
  searchName: any ='';
  status: any='';
  sortingBy: any ='';
  sortingDir: any = '';  
  event : any = 0 ;

  //'tickets-app?tenantId='+this.users.tenantId+'&userId='+ this.users.userId   
  //  app-ticketss

   


  getTickets() {
    this.auth.openSpinner();    
      this.auth.queryService('tickets-app?tenantId='+this.users.tenantId+'&userId='+ this.users.userId).then((data) => {
        this.auth.closeSpinner(); 
        this.ticketList = data;
        if(this.ticketList ){
          this.dataFetched = true;
        }
        this.filteredOrderList = this.ticketList;
         
      }).catch((err) => {
        this.auth.closeSpinner();
        console.log(err);
      });     
  }

  backNav(){
    if(this.userRole == 'STOCKIST'){
      this.router.navigateByUrl('/common-dashboard/home');
      this.dashboard.onTabChange('home');
    }
    else if(this.userRole == 'USER'){
      this.router.navigateByUrl('/common-dashboard/user-home');
      this.dashboard.onTabChange('home');
    }
  }

  // ticketId: any;
  ticketDetail(ticketId: any){
    this.router.navigate(['/ticket-details' , { ticketNo: ticketId }]);  
  }
}
