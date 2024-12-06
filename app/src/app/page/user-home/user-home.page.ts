import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BarcodeScanner } from '@capacitor-mlkit/barcode-scanning';
import { AlertController, NavController, Platform } from '@ionic/angular';
import * as Highcharts from 'highcharts';
import { AuthService } from 'src/app/services/auth.service';
import { HelperService } from 'src/app/services/helper.service';
import { CommonDashboardPage } from 'src/app/page/common-dashboard/common-dashboard.page';
import { Subscription } from 'rxjs';
import { App } from '@capacitor/app';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.page.html',
  styleUrls: ['./user-home.page.scss'],
  providers:[AuthService]

})
export class UserHomePage implements OnInit, OnDestroy {

  name: string = 'mohana';
  Highcharts: typeof Highcharts = Highcharts;
  showCustomAlert = false;
  scannedNumber : any;
  serialNumber!: string;
  scannedData: any;

  tenantId: any;
  private lastBackTime = 0;
  backButtonSubscription: Subscription = new Subscription; 



  chartOptions: Highcharts.Options = {
    series: [
      {
        data: [1, 2, 3],
        type: 'line',
      },
    ],
  };
  Pollutant: any

  constructor(public dashboard: CommonDashboardPage, public navCtrl: NavController, private platform : Platform,   public alertController: AlertController, private router: Router, private auth:AuthService, private helper: HelperService) {
    this.googleModule();
  }  

  userName: any;
  user: any;
  ngOnInit() {
    console.log('h');
    this.userName= localStorage.getItem('user_name');
    this.tenantId = localStorage.getItem('tenant_id');
    let u : any = localStorage.getItem('etraze_user');
    this.user = JSON.parse(u);


    this.Pollutant = {
      chart: {
        type: 'column'
      },
      colors: ['#FFA500', '#8A2BE2'],
      title: {
        text: null,
      },
      xAxis: {
        categories: ['SUN', 'MON ', 'TUE', 'WED', 'THURS', 'FRI', 'SAT'],
        crosshair: true,
        accessibility: {
          description: 'Countries'
        }
      },
      yAxis: {
        min: 0,
        // title: {
        //   text: '1000 metric tons (MT)'
        // }
      },
      tooltip: {
        valueSuffix: ' (1000 MT)'
      },
      plotOptions: {
        column: {
          pointPadding: 0.2,
          borderWidth: 0
        }
      },
      credits:{
        enabled:false
      },

      series: [
        {
          name: 'Tickets',
          color: '#FFA500',
          data: [98, 23, 67, 55, 43, 78]
        },
        { 
          name: 'Orders',
          color: '#8A2BE2',
          data: [64, 43, 80, 36, 56, 65]
        }
      ]
    }; 
    this.googleModule();
  }

 
  ionViewWillEnter(){
    this.backButtonSubscription = this.platform.backButton.subscribeWithPriority(10, () => {
       console.log('back button 3');
       this.helper.presentAlert('Do you want to exit the app');
     });
   }

   ionViewWillLeave(){
    console.log('will leave called in back button 3');
    this.backButtonSubscription.unsubscribe();
   }

   ngOnDestroy(): void {
     console.log('destroy');
   }
   

   private handleBackButton() {
    const now = new Date().getTime();
    const timeDiff = now - this.lastBackTime;

    if (timeDiff < 2000) { // Check if less than 2 seconds between taps
      // Exit the app or perform your desired action
      App.minimizeApp();
    } else {
      // Inform the user to tap again to exit
      this.helper.exit('tap again to exit');
      this.lastBackTime = now;
    }    
  }

  // user summary api

  userSummary:any;
  getSummary(){
    this.auth.openSpinner();
    this.auth.queryService('user-summary').then((data) => {
      this.auth.closeSpinner();
      this.userSummary = data;
    console.log('user summary',this.userSummary);

    }).catch((err) =>{
      this.auth.closeSpinner();
      console.log(err);
    })
  }
  
  date = "12-02-2002";
  dateWithTime: any ; 
  dateNow(){
    let currentTime = new Date().toLocaleTimeString();
    console.log('timesstamp ' , currentTime);
    this.dateWithTime = currentTime ;
  }

  data: any;
  googleModule() {
    BarcodeScanner.isGoogleBarcodeScannerModuleAvailable().then(result => {
      if (!result.available) {
        BarcodeScanner.installGoogleBarcodeScannerModule().then(() => {
          console.log('Google Barcode Scanner Module installed successfully.');
        }).catch(error => {
          console.error('Error installing Google Barcode Scanner Module:', error);
        });
      } else {
        console.log('value: Google Barcode Scanner Module is already available.');
      }
    });
  }
  isGoogleBarcodeScannerModuleAvailable = async () => {
    const { available } =
      await BarcodeScanner.isGoogleBarcodeScannerModuleAvailable();
    return available;
  };

  installGoogleBarcodeScannerModule = async () => {
    await BarcodeScanner.installGoogleBarcodeScannerModule();
  };

  qrData = {  
    ticketNumber: '',  
    invoiceNumber: '',  
    tenantId: '',
    type: ''  
  };

  scanBarcode = async () => {
    try {
      const result = await BarcodeScanner['scan']();      
      if (result.barcodes != null) {       
        this.scannedNumber = result.barcodes[0].rawValue;
        this.qrData = JSON.parse(this.scannedNumber);
        if(this.qrData.tenantId === this.tenantId){
          if(this.qrData.ticketNumber.includes('TKT')){                       
            if(this.user.userAppType == "ORDER" || this.user.userAppType == "BOTH"){  
              this.router.navigate(['/user-order'], {
                queryParams: {
                  ticketNo: this.qrData.ticketNumber,
                  invoiceNo: this.qrData.invoiceNumber,
                  tenantId: this.qrData.tenantId,
                  type: this.qrData.type,
                }
              });   
              } else {
              this.helper.presentErrorToast('This user does not have permission to process order');
            }                    
          } else{
              this.helper.presentErrorToast("this is not a valid number");
          } 
        } else {
          this.helper.presentErrorToast('this user not able to process the order');
        }             
      }
    } catch (error) {
        console.error('Barcode scanning error:', error);
    }
  };

  // scanQr = async () => {
  //   try {
  //     const result = await BarcodeScanner['scan']();
  //     if (result.barcodes != null) {
       
  //       this.serialNumber = result.barcodes[0].rawValue;
  //       console.log('serial number', this.serialNumber);       
  //       this.router.navigate(['/user-return' , { ticketNo: this.serialNumber }]);
        
  //     }
  //   } catch (error) {
  //     console.error('Barcode scanning error:', error);
  //   }
  // };

  

  tickets(){
    this.dashboard.onTabChange('tickets');
    this.router.navigateByUrl('/common-dashboard/tickets');
  }
  orders(){
    this.dashboard.onTabChange('orders');
    this.router.navigateByUrl('/common-dashboard/orders');
  }
  
  tempRoute(){
    this.router.navigateByUrl('/cheque-details');   
  }

}
