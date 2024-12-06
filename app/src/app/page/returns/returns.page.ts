import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { AuthService } from 'src/app/services/auth.service';
import { CommonDashboardPage } from 'src/app/page/common-dashboard/common-dashboard.page';
import { Browser } from '@capacitor/browser';
import { NavController, Platform } from '@ionic/angular';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-returns',
  templateUrl: './returns.page.html',
  styleUrls: ['./returns.page.scss'],
})
export class ReturnsPage implements OnInit,OnDestroy {
  returnList: any;
  userRole: any;
  selectedStatus = ''
  filteredOrderList: any;
  daysDifference: any;
  date1: any;
  date2: any;
  days: any;
  dataFetched : boolean = false;

  sts: string =  'PENDING';
  user : any ;

  backButton: Subscription = new Subscription;
  

  constructor(public dashboard: CommonDashboardPage,private platform: Platform,public navCtrl: NavController ,  private auth: AuthService, private router: Router) {
  }

  ngOnInit() {
    this.userRole = localStorage.getItem("user_role")
    let u : any = localStorage.getItem('etraze_user');
    this.user = JSON.parse(u);  
    this.getReturns(this.sts,''); 

  }

  ionViewWillEnter(){
    this.backButton = this.platform.backButton.subscribeWithPriority(12,()=>{
      console.log('backbutton 4');
      if(this.userRole == 'MANAGER'){
        this.router.navigateByUrl('/common-dashboard/home');
        this.dashboard.onTabChange('home');
      } else {
        this.router.navigateByUrl('/common-dashboard/user-home');
        this.dashboard.onTabChange('home');
      }
    });
  }
  ionViewWillLeave(){
    console.log('inside return ion view will leave');
    this.backButton.unsubscribe();
    // this.backNav();
  }
  ngOnDestroy(): void {
    console.log('destroy');
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

  onSearch(event: any) {
    const searchTerm = event.target.value.toLowerCase();
    if (!searchTerm.trim()) {
      this.filteredOrderList = this.returnList.slice();
    } else {
      this.filteredOrderList = this.returnList.filter((d: any) => d.claimNumber.toLowerCase().includes(searchTerm));      
    }
}

  filterOrders(status: string) {
    this.selectedStatus = status;
    if (status === 'All') {
      this.filteredOrderList = this.returnList.slice();
    } else {
      this.filteredOrderList = this.returnList.filter((order: { status: string; }) => order.status === status);
    }
  }

  filter(){

  }

  claims: any;

  getClaims(){
    this.auth.queryService('claims-app').then((data : any) =>{
      this.claims = data?.data.claims;
    }).catch((err)=>{
      console.log(err);
    })
  }

  //claims-app
  typeOfGrrn: any ; 
  getReturns(sts: any, type: any) {
    this.auth.openSpinner();
    this.typeOfGrrn = type ;
    console.log(this.typeOfGrrn);
    this.auth.queryService('returns-mobile?status='+ sts +'&type='+type ).then((data: any) => {
      this.auth.closeSpinner(); 
      this.returnList = data;  //.data.claims
      if(this.returnList){
        this.dataFetched = true;
      }
      this.filteredOrderList = this.returnList;  
          
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    });
  }

  processReturn(){

  }

  backNav(){
    this.dashboard.onTabChange('home');
    if(this.userRole == 'MANAGER'){
      this.router.navigateByUrl('/common-dashboard/home');    
    } else {
      this.router.navigateByUrl('/common-dashboard/user-home');
    }

  }

 /**ticketNo: sn */

  return(sn:any , type: any ){
    console.log('return Id ',sn);
    // this.router.navigate(['/user-return'] , {
    //   queryParams: {
    //     ticketNo: sn ,
    //     grrnType: type
    //   }
    // });
    this.navCtrl.navigateForward(['/user-return'], {
      queryParams: {
        ticketNo: sn ,
        grrnType: type
      }
    });
  }

  sNumber: any;
  newForm(){
    if(this.userRole == 'USER'){
      this.auth.openSpinner();
      this.auth.queryService('serial-number').then((data) => {
        this.auth.closeSpinner();
        this.sNumber = data;
        if(this.sNumber.code == '0000'){
          this.router.navigate(['/user-return', {ticketNo: this.sNumber.data}]);
        }
      }).catch((err) => {
        this.auth.closeSpinner();
        console.log('-------------',err);
      })
    } else {
      console.log('you are a MANAGER');
    }

  }

  handleRefresh(event : any) {
    setTimeout(() => {
      if(this.userRole == 'STOCKIST'){
        
      } else {
        this.getReturns('PENDING','');
      }
      event.target.complete();
    }, 1500);
  }
}
