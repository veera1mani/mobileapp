import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { Geolocation } from '@capacitor/geolocation';
import { AndroidSettings, IOSSettings, NativeSettings } from 'capacitor-native-settings';
import { HelperService } from 'src/app/services/helper.service';
import { AlertController, NavController, Platform } from '@ionic/angular';
import { Subscription } from 'rxjs';
import { NativeGeocoder, NativeGeocoderResult, NativeGeocoderOptions } from '@awesome-cordova-plugins/native-geocoder/ngx';



@Component({
  selector: 'app-transport-order',
  templateUrl: './transport-order.page.html',
  styleUrls: ['./transport-order.page.scss'],
})
export class TransportOrderPage implements OnInit, OnDestroy {

  ticketId: any;
  receivedstatus: any;
  position: any;
  backButtonSubscription: Subscription = new Subscription;
  inv: any;
  priority: any ;
  scanTicketId: any;
  type: any;
  tickets: any = [];

  constructor(private router: Router, private platform: Platform , private navCtrl: NavController , private nativeGeocoder: NativeGeocoder ,    public alertController: AlertController, private helper: HelperService, private route: ActivatedRoute, private auth: AuthService) {

  }


 
  ngOnInit() {
    // this.ticketId = this.route.snapshot.paramMap.get('ticketNo');
    this.route.queryParams.subscribe(params => {
      this.scanTicketId = params['ticketNo'];
      this.inv = params['invoiceNo'];
      const tenantId = params['tenantId']; 
      this.type = params['type'];     
    }); 
    if(this.type == 'PICKING'){
      this.ticketId = this.scanTicketId;
      this.helper.invalidOrder('invalid QR');
      this.router.navigateByUrl('/common-dashboard/transporter');
    } else if (this.type == 'carton') {
      if(this.scanTicketId.includes(',')){
        this.tickets = this.scanTicketId.split(',');
        this.ticketId = this.tickets[0];
      } else {
        this.ticketId = this.scanTicketId;
      }
    }
    this.getOrderDetails(this.ticketId);
    this.getInvoiceDetails(this.ticketId);
    let invoice = {
      ticketId: this.ticketId,
      invoiceNumber: this.inv
    }
    this.getInvoice(invoice);
    this.getCurrentLocation();
  }
  ionViewWillEnter(){
    this.backButtonSubscription = this.platform.backButton.subscribeWithPriority(19,()=>{
      console.log('back buttton 10');
      this.navCtrl.navigateBack('/common-dashboard/transporter');
    });
  }
  ionViewWillLeave(){
    this.backButtonSubscription.unsubscribe();
  }

  ngOnDestroy(): void {
    console.log('destroy');
  }

  backNav(){
    this.router.navigateByUrl('/common-dashboard/transporter');
  }

  public counts = ["Email Received on ","Invoice Created","Dispatched","Deliverd"];
  public orderStatus = "In Progress"


  orderDetails:any;
 
  getOrderDetails(ticketId:string){
    this.auth.openSpinner();
    this.auth.queryService('ticket-details/'+ticketId).then((data: any) => {
      this.auth.closeSpinner();
      this.orderDetails = data.data;   
      this.receivedstatus = this.orderDetails.ticket.status;
      if(this.receivedstatus == 'PICKED' || this.receivedstatus == 'CHECKED' || this.receivedstatus == 'PACKED'){
        this.helper.invalidOrder('this order is not dispatched yet');
        this.router.navigateByUrl('common-dashboard/transporter');
      } else if (this.receivedstatus == 'DELIVERED' || this.receivedstatus == 'POD RECEIVED'){
        this.helper.invalidOrder('this order is already Delivered');
        this.router.navigateByUrl('common-dashboard/transporter');
      } else {
        console.log('inside dispatch status');
      }
      
    }).catch((err) =>{
      this.auth.closeSpinner();
      console.log(err);
    })
  }

  

  order = ({
    ticketId: '',
    status: '',
    latitude:'',
    longitude: '',
    invoice: '',
    historyId: '',
    address: ''
  });
   // update status of order
   //'update-ticket'
   //'update-status'
 updateOrderStatus( ser: any , status:string){
  this.auth.openSpinner();
  this.order.ticketId = this.ticketId;
  this.order.invoice = this.inv;
  this.order.status = status;
  this.getCurrentLocation();
  if(this.order.latitude  && this.order.longitude){
    this.order.address = this.address.addressLines[0];
    console.log('address' , this.order.address);
    this.auth.updateService(ser, this.order).then((data: any) => {
      this.auth.closeSpinner();
      if(data?.code == '0000'){
        this.helper.presentToast('Order Delivered Successfully');
        this.router.navigateByUrl('/common-dashboard/transporter');
      } else {
        this.helper.presentErrorToast("Sorry , didn't delivered");
      } 
    }).catch((err) =>{
      this.auth.closeSpinner();
      console.log(err);
    });

  } else {
    this.helper.presentErrorToast('Location is not granted');
    this.auth.closeSpinner();
  }

}

  hisId: any;
  historyId(inv: any){
    
    for(let i=0 ; i<=this.orderDetails?.history.length ; i++){
      if(this.orderDetails.history[i].invoice != null){
        if(inv == this.orderDetails.history[i].invoice && this.orderDetails.history[i].status == 'DISPATCHED'){
          this.order.historyId = this.orderDetails.history[i].id ;
          return
        }
      }
    }
  }

  delivery(){
    if(this.invoice?.priority){
      this.order.invoice = this.inv ;
      this.historyId(this.inv);
      // this.order.historyId = this.hisId.id ;
      this.updateOrderStatus('update-status-priority' , 'DELIVERED');
    } else {
      this.updateOrderStatus('update-status' , 'DELIVERED');
    }   
  }


  submit(){
    console.log('submit button');
  }
  invoiceDetails: any;
  invoiceNo: any='' ;  
  numOfCases: any='';
  
  getInvoiceDetails(ticketId:string){
    this.auth.openSpinner();
    this.auth.queryService('ticket-order-invoices/'+ticketId).then((data :any) => {
      this.auth.closeSpinner();
      this.invoiceDetails = data.data;
      
      for(let i=0 ;  i<this.invoiceDetails?.invoices.length; i++){
        if(i==0){
          this.invoiceNo = this.invoiceNo +' '+ this.invoiceDetails.invoices[i].invoiceNumber;
          this.numOfCases = this.numOfCases +' '+ this.invoiceDetails.invoices[i].numOfCases;
        }else{
          this.invoiceNo = this.invoiceNo +','+ this.invoiceDetails.invoices[i].invoiceNumber;
          this.numOfCases = this.numOfCases +','+ this.invoiceDetails.invoices[i].numOfCases;
        }
      }

      console.log('invoice details'+this.invoiceNo);
    }).catch((err) =>{
      this.auth.closeSpinner();
      console.log(err);
    });
  }

  invoice: any;
  getInvoice(inv: any){
    this.auth.openSpinner();
    this.auth.createService('ticket-order-invoice',inv).then((data : any) => {
      this.auth.closeSpinner();
      this.invoice = data.result;
      if(this.invoice?.packId != null){
        this.getNumOfCases(this.invoice.packId);
      }

    }).catch((err) =>{
      this.auth.closeSpinner();
      console.log(err);
    });
  }

  getNumOfCases(packId: any){
    this.auth.queryService('no-of-cases?packId='+ packId).then((data: any) => {
      this.cases = data.data;
      
      console.log('this.cases',this.cases);
    }).catch((err: any) => {
      console.log(err);
    });
  }

  numOfCase: any[] = [];
  cases : any ;
  numberOfCases : any ;
  getNumOfCase(){
    let inv = this.orderDetails.orderDTO.invoices;    
    for(let invo of inv){  
      this.numOfCase.push( parseInt(invo?.numOfCases));      
      let numbers = this.numOfCase.map(Number).filter(num => !isNaN(num));      
      let totalNumOfCases = numbers.reduce((sum, numOfCases) => sum + numOfCases, 10);
      this.cases = totalNumOfCases ;
    }   
  }



  // printCurrentPosition = async () => {
  //   const positionOptions = {
  //     enableHighAccuracy: true, // This is similar to enableHighAccuracy in Cordova
  //   };      
  //   const coordinates = await Geolocation['getCurrentPosition'](positionOptions);
  //   console.log('coordinates : ', coordinates);
  //   this.order.latitude =  coordinates.coords.latitude.toString();
  //   this.order.longitude =  coordinates.coords.longitude.toString();
  // };

  
  latLong: any;
  async getCurrentLocation(){
    try{
      const permissionStatus = await Geolocation.checkPermissions();
      if(permissionStatus.location != 'granted'){
        const requestStatus = await Geolocation.requestPermissions(); //checkPermissions
        if(requestStatus.location != 'granted'){
          await this.openSettings(true);
        } 
      }
      let options : PositionOptions = {
        maximumAge : 3000,
        timeout : 10000, 
        enableHighAccuracy : true
      }
      this.latLong = await Geolocation['getCurrentPosition'](options);
      console.log('coordinates : ', this.latLong);
      this.order.latitude =  this.latLong.coords.latitude.toString();
      this.order.longitude =  this.latLong.coords.longitude.toString();
      } catch(e: any){      
      if(e?.message == 'Location services are not enabled'){
        this.presentAlert();
      }
      console.log('catch',e);
      throw(e);
    }
    
    let options: NativeGeocoderOptions = {
      useLocale: true,
      maxResults: 5
    };
    this.nativeGeocoder.reverseGeocode(this.latLong.coords.latitude, this.latLong.coords.longitude, options)
      .then((result: NativeGeocoderResult[]) =>{
        console.log('address ::::::: ',JSON.stringify(result[0]));
        this.address = result[0];
      }).catch((error: any) => console.log(error));

  };

  address: any ;
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
      mode: 'ios',
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
