import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BarcodeScanner } from '@capacitor-mlkit/barcode-scanning';
import { AlertController, NavController } from '@ionic/angular';
import * as Highcharts from 'highcharts';
import { CommonDashboardPage } from 'src/app/page/common-dashboard/common-dashboard.page';
import { HelperService } from 'src/app/services/helper.service';


@Component({
  selector: 'app-home-bottom-navi',
  templateUrl: './home-bottom-navi.page.html',
  styleUrls: ['./home-bottom-navi.page.scss'],
  
})
export class HomeBottomNaviPage implements OnInit {
  name: string = 'Veera';
  Highcharts: typeof Highcharts = Highcharts;
  chartOptions: Highcharts.Options = {
    series: [
      {
        data: [1, 2, 3],
        type: 'line',
      },
    ],
    title: {
      text: '' ,
    },
  };
  Pollutant: any;
  userName: any;
  scannedNumber: any;
  tenantId: any;
  user: any;

  
  constructor(public dashboard: CommonDashboardPage,public helper: HelperService ,  public alertController: AlertController, public navCtrl: NavController, private router: Router ) { 


  }

  async presentLogoutAlert() {
    
  }
  ngOnInit() {
    this.userName= localStorage.getItem('user_name');
    this.tenantId = localStorage.getItem('tenant_id');
    let u : any = localStorage.getItem('etraze_user');
    this.user = JSON.parse(u);

    this.Pollutant = {
      label: { enabled: false },
      xAxis: {
        accessibility: {
          rangeDescription: 'Range: 2010 to 2020',
        },
      },

      legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
      },
      
      plotOptions: {
        series: {
          label: {
            connectorAllowed: false,
          },
          pointStart: 2010,
        },
      },

      series: [
        {
          name: 'Installation & Developers',
          data: [
            43934, 48656, 65165, 81827, 112143, 142383, 171533, 165174, 155157,
            161454, 154610,
          ],
        },
        {
          name: 'Other',
          data: [
            21908, 5548, 8105, 11248, 8989, 11816, 18274, 17300, 13053, 11906,
            10073,
          ],
        },
      ],
      title: {
        value:''
       },
      credits:{
        enabled:false
      },

      responsive: {
        rules: [
          {
            condition: {
              maxWidth: 500,
            },
            chartOptions: {
              legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'bottom',
              },
            },            
          },
        ],
      },
    };
    this.googleModule();
  }
  
  isModalOpen = false;

  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

  confirmLogout() {
    // Implement your logout logic here
    console.log('Logout confirmed');
    this.closeModal();
  }

  openNoti() {
    this.router.navigateByUrl('/common-dashboard/home/Notification');
  }

  showCustomAlert = false;

  openCustomAlert() {
    this.showCustomAlert = true;
  }

  closeCustomAlert() {
    this.showCustomAlert = false;
  }

  tickets(){
    this.dashboard.onTabChange('tickets');
    this.router.navigateByUrl('/common-dashboard/tickets');
  }
  orders(){
    this.dashboard.onTabChange('orders');
    this.router.navigateByUrl('/common-dashboard/orders');
  }
  returns(){
    this.dashboard.onTabChange('returns');
    this.router.navigateByUrl('/common-dashboard/returns');
  }
  cheques(){
    this.dashboard.onTabChange('cheques');
    this.router.navigateByUrl('/common-dashboard/cheques');
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
    tenantId: '' ,
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
              this.router.navigate(['/user-order'], {
                queryParams: {
                  ticketNo: this.qrData.ticketNumber,
                  invoiceNo: this.qrData.invoiceNumber,
                  tenantId: this.qrData.tenantId,
                  type: this.qrData.type
                }
              });                                
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

}
