import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { AuthService } from 'src/app/services/auth.service';
import { CommonDashboardPage } from 'src/app/page/common-dashboard/common-dashboard.page';
import { HelperService } from 'src/app/services/helper.service';


@Component({
  selector: 'app-cheques',
  templateUrl: './cheques.page.html',
  styleUrls: ['./cheques.page.scss'],
})
export class ChequesPage implements OnInit {

  chequeList: any;
  userRole: any;
  selectedStatus = ''
  filteredOrderList: any;
  searchedList: any;
  daysDifference: any;
  dataFetched: boolean = false ;

  constructor(public dashboard: CommonDashboardPage, private auth: AuthService, private router: Router, private helper: HelperService) {
  }

  ngOnInit() {
    this.userRole = localStorage.getItem("user_role")
    console.log('init');
    // this.getCheques();

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
    }    
    return this.daysDifference;

  }

  searchTerm: any;
  onSearch(event: any) {
     this.searchTerm = event.target.value;
    // if (!searchTerm.trim()) {
    //   this.filteredOrderList = this.chequeList.slice();
    // } else {
    //   this.filteredOrderList = this.chequeList.filter((d: any) => d.chequeNumber.includes(searchTerm) > 0 );      
    //   this.searchedList = this.filteredOrderList ;
    // }
  }

  search(){   
    if(this.searchTerm){
    console.log('searchterm : ',this.searchTerm);      
    } else {
      this.helper.presentErrorToast('search field is empty');
    }
    this.getCheques();
  }

  filterOrders(status: string) {
    this.selectedStatus = status;
    if (status === 'All') {
      this.filteredOrderList = this.chequeList.slice();
    } else {
      this.filteredOrderList = this.chequeList.filter((order: { status: string; }) => order.status === status);
      this.searchedList = this.filteredOrderList;
    } 
  }

  getCheques() {
    this.dataFetched = true;
    this.auth.openSpinner();
    this.auth.queryService('cheque-number/'+this.searchTerm).then((data) => {
      this.auth.closeSpinner(); 
      this.chequeList = data;
      if(this.chequeList){
        this.dataFetched = false ; 
      } else {
        this.dataFetched = false;
      }
      this.filteredOrderList = this.chequeList.slice().reverse();
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    })
  }

  backNav(){
    this.dashboard.onTabChange('home');
    this.router.navigateByUrl('/common-dashboard/home');
  }

  // chequeDetail(chequeId: any){  
  //   this.router.navigate(['/cheque-details' , { ticketNo: chequeId ,StockistId: this.chequeList.stockistId }]);  
  // }
}
