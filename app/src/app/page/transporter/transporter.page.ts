import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BarcodeScanner } from '@capacitor-mlkit/barcode-scanning';
import { AuthService } from 'src/app/services/auth.service';
import { HelperService } from 'src/app/services/helper.service';
import { Geolocation } from '@capacitor/geolocation';
import { AndroidSettings, IOSSettings, NativeSettings } from 'capacitor-native-settings';
import { Capacitor } from '@capacitor/core';
import { AlertController, NavController, Platform } from '@ionic/angular';
import { Subscription } from 'rxjs';



@Component({
  selector: 'app-transporter',
  templateUrl: './transporter.page.html',
  styleUrls: ['./transporter.page.scss'],
})  
export class TransporterPage implements OnInit, OnDestroy {

  data: any;
  name: any;
  qrData: any;
  position: any;
  recentActivity: any;
  backBtnSubscription: Subscription = new Subscription;
  user: any;

  constructor(private router: Router, private platform: Platform , private navCtrl: NavController  ,  private alertController: AlertController, private auth: AuthService, private helper: HelperService) {
    this.googleModule();
      this.sss();
   }

   async sss(){
    const permissionStatus = await Geolocation.checkPermissions();
    console.log('permission status ::' , permissionStatus.location);
      const requestStatus = await Geolocation.requestPermissions(); //checkPermissions
   }

  ngOnInit() {
    console.log('init');
    this.name = localStorage.getItem('user_name');
    let u : any = localStorage.getItem('etraze_user');
    this.user = JSON.parse(u);
    this.getSummary();
    this.getCurrentLocation();
    this.getRecentActivity();
  }

  ionViewWillEnter(){
    this.backBtnSubscription = this.platform.backButton.subscribeWithPriority(18 , ()=>{
      console.log('back button 13');
       this.helper.presentAlert('Do you want to exit the app');
    });
  }
  ionViewWillLeave(){
    this.backBtnSubscription.unsubscribe();
  }

  ngOnDestroy(): void {
    console.log('destroy');
  }

  googleModule(){    
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

  transporterSummary:any;
  getSummary(){
    this.auth.openSpinner();
    this.auth.queryService('transport-order').then((data) => {
    this.auth.closeSpinner();
    this.transporterSummary = data;
    console.log(this.transporterSummary);
  }).catch((err) =>{
    this.auth.closeSpinner();
    console.log(err);
  })
 }

 getRecentActivity(){
  this.auth.queryService('tenant-transporter-orders').then((data)=>{
    this.recentActivity = data;
    console.log(this.recentActivity);
  }).catch((err) => {
    console.log(err);
  });
 }

scannedNumber!:string;
scanBarcode = async () => {
  try {
    const result = await BarcodeScanner['scan']();
    if (result.barcodes != null) {
      console.log("Value: " + result.barcodes[0].rawValue);
      this.scannedNumber = result.barcodes[0].rawValue;
      this.qrData = JSON.parse(this.scannedNumber);
      
      if(this.user.tenantId == this.qrData.tenantId){
        if(this.qrData.ticketNumber.includes('TKT')){
          this.router.navigate(['/transport-order'], {
          queryParams: {
            ticketNo: this.qrData.ticketNumber,
            invoiceNo: this.qrData.invoiceNumber,
            tenantId: this.qrData.tenantId,
            type: this.qrData.type,
          }
        });
        }else{
          this.helper.presentErrorToast("this is not a valid number");
        }
      } else{
        this.helper.presentErrorToast('This user does not belongs to this tenant');
      }  
  }
  
  }catch (error) {
    console.error('Barcode scanning error:', error);
  }
};

  justRoute(){
    this.router.navigate(['/transport-order' , { ticketNo: 'TKT2024010425' }]); 
  }
  
  setResult(ev:any) {
    console.log(`Dismissed with role: ${ev.detail.role}`);
  }

  async getCurrentLocation(){
    try{
      const permissionStatus = await Geolocation.checkPermissions();
      console.log('permission status ::' , permissionStatus.location);
      if(permissionStatus.location != 'granted'){
        this.helper.presentErrorToast('permission status is not granted');
        const requestStatus = await Geolocation.requestPermissions(); //checkPermissions
        console.log('requeststatus : ', requestStatus.location);
        if(requestStatus.location != 'granted'){
          this.helper.presentErrorToast('location is not granted');
          await this.openSettings(true);
          console.log('inside open settings true');
        } 
      }else if( permissionStatus.location == 'granted') {
        this.helper.presentToast('location is granted');
      }
      let options : PositionOptions = {
        maximumAge : 3000,
        timeout : 10000, 
        enableHighAccuracy : true
      }
       this.position = await Geolocation.getCurrentPosition(options);
      console.log('position', this.position);
    }
    catch(e: any){
      if(e?.message == 'Location services are not enabled'){
        this.presentAlert();
      }
      console.log('catch',e);
      throw(e);
    }    
  };

  openSettings(app = false){
    console.log('open settings ....');
    return NativeSettings.open({
      optionAndroid: app ? AndroidSettings.ApplicationDetails : AndroidSettings.Location, 
      optionIOS: IOSSettings.App
    })
  }

  async presentAlert() {
    const alert = await this.alertController.create({
      header: 'Alert',      
      message: 'Transporter wants location access to deliver the order',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          cssClass: 'secondary',
          handler: () => {
            console.log('Cancel clicked');
            this.helper.presentErrorToast('transporter must allow location ');
          }
        },
        {
          text: 'OK',
          handler: () => {
            console.log('OK clicked');
            this.openSettings();
          }
        }
      ]

    });
  
    await alert.present();
  }



  

}

  

