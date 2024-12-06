import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertController, NavController, Platform } from '@ionic/angular';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { HelperService } from 'src/app/services/helper.service';
import { CommonDashboardPage } from '../common-dashboard/common-dashboard.page';


interface Order {
  ticketId: string;
  status: string;
  transporter: string;
  vehicaleNo: string;
  numOfCases: string;
  invoiceNumber: string;
  selectList: string;
  isSelected: boolean;
  priority : boolean;
}

@Component({
  selector: 'app-user-order',
  templateUrl: './user-order.page.html',
  styleUrls: ['./user-order.page.scss'],
})
export class UserOrderPage implements OnInit , OnDestroy {

  receivedstatus: any;
  ticketStatus: any;
  users: any;  
  ticketId!:any;
  updateData: any;
  status = false;
  orderDetails:any;

  packedStatus!: FormGroup; 
  transport!: FormGroup; 

  transporter: any;
  selectedTransporterId: any;

  submitFlag: boolean = false;
  dispatchButton: boolean = false;
  
  scannedData: any;
  user: any;
  scannedTicketId: any;
  tickets : any = [];
  type: any;

  backButtonSubscription: Subscription = new Subscription;


  viewStatus(){
    if(this.status){
      this.status = false;
    }else{
      this.status = true;
    }
  }
  constructor(private router: Router,  private platform: Platform  ,   private alertController : AlertController ,  private auth: AuthService, private route: ActivatedRoute, public navCtrl: NavController, private fb: FormBuilder, private helper: HelperService)
  {
    this.packedStatus = this.fb.group({      
      numOfCases:[null, Validators.required]
    });
    
    this.transport = this.fb.group({
      transporter: [null, Validators.required],     //   
      vehicaleNo: [null, Validators.required]
    });
  }

  qrData = {  
    ticketNumber: '',  
    invoiceNumber: '',  
    tenantId: ''  ,
    type: ''
  };
  
  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const ticketNo = params['ticketNo'];   //  ticketNo
      const invNo = params['invoiceNo'];
      const tenantId = params['tenantId'];
      const type = params['type'];
      console.log(ticketNo);      
      this.scannedTicketId = ticketNo;
      this.invoiceNo = invNo;
      this.type = type;
    });   
    if(this.type == 'PICKING'){
      this.ticketId = this.scannedTicketId;
    } else if (this.type == 'carton') {
      if(this.scannedTicketId.includes(',')){
        this.tickets = this.scannedTicketId.split(',');
        this.ticketId = this.tickets[0];
      } else {
        this.ticketId = this.scannedTicketId;
      }
    }
    
    
    let u : any = localStorage.getItem('etraze_user');
    this.user = JSON.parse(u);
    this.getOrderDetails(this.ticketId);
    this.getusers();
    let invoice = {
      ticketId: this.ticketId,
      invoiceNumber: this.invoiceNo
    }
    this.getInvoiceDetails(invoice);   
  }

  ionViewWillEnter(){
    this.backButtonSubscription = this.platform.backButton.subscribeWithPriority(15, () =>{ 
      console.log('back button 7');     
      this.navCtrl.navigateBack('/common-dashboard/home');      
    });
  }
  ionViewWillLeave(){
    this.backButtonSubscription.unsubscribe();
  }

  ngOnDestroy(): void {
    console.log('destroy');
  }

  id: any = localStorage.getItem("etraze_ticketid");

  backNav(){
    this.router.navigateByUrl('/common-dashboard/home');
  }


  // after scan api call for get a order  
  getOrderDetails(ticketId:string){
    this.auth.openSpinner();
    this.auth.queryService('ticket-details/'+ticketId).then((data: any) => {
      this.auth.closeSpinner();
      this.orderDetails = data.data?.ticket; 
      this.ticketStatus = this.orderDetails?.status; 
      if(data?.code == '2222'){
        this.navCtrl.pop();
        this.helper.presentErrorToast('This user not mapped with this manufacturer');
      }
    }).catch((err) =>{
      this.auth.closeSpinner();
      console.log(err);
    });
  }   
  order: Order = {
    ticketId: '',
    status: '',
    numOfCases:'',
    transporter: '',
    vehicaleNo: '',
    invoiceNumber: '',
    selectList: '',
    isSelected: false,
    priority: false ,
  };

  priority : boolean = false;
  onToggleChange(e: any){
    console.log('toggle status',e);
    console.log('toggle status',e.detail.checked);
    if(e.detail.checked){
      this.priority = true;
    } else {
      this.priority = false ; 
    }
  }

 

  vehicaleNo: any;
  // update status of order
  // update-ticket

  dispatchStatus = {
    ticketId: '',
    status: '',
    numOfCases:'',
    transporter: '',
    vehicaleNo: '',
    invoice: '',
    selectList: '',
    isSelected: false,
    priority: false ,
  }

  dispatch(){
    if(this.type == 'carton'){
      // if(this.invoiceDetails?.priority){
      //   this.updateApi('update-status-priority' , 'DISPATCHED');
      // } else {
      //   this.updateApi('update-status' , 'DISPATCHED');
      // }
      this.updateApi('update-order-dispatch' , 'DISPATCHED');
    } else {
      this.helper.invalidOrder('invalid QR');
    }
  }
 
  updateApi(ser: any , status:string){
    this.auth.openSpinner();
    this.dispatchStatus.ticketId=this.ticketId;
    this.dispatchStatus.status = status;
    this.dispatchStatus.priority = this.invoiceDetails?.priority;
    console.log('priority   ::: ', this.order.priority);
    this.dispatchStatus.invoice = this.invoiceDetails?.invoiceNumber ; 
      this.auth.updateService(ser, this.dispatchStatus).then((data) => {
      this.auth.closeSpinner();
      this.updateData = data;
      if(this.updateData.code == '0000'){  
        this.helper.presentToast("Order is "+status.toLowerCase() +" successfully");
        if(this.user.roleName == 'USER'){
          this.navCtrl.navigateRoot('/common-dashboard/user-home'); 
        }else {
          this.navCtrl.navigateRoot('/common-dashboard/home');
        }
      }else if(this.updateData.code == '2222'){
        this.helper.presentErrorToast('Transporter not assigned');
      }
    }).catch((err) =>{
      this.auth.closeSpinner();
      console.log(err);
    });
  }

 updateInv(status:string){
  this.auth.openSpinner();
  this.order.ticketId=this.ticketId;
  this.order.status = status;
  this.order.invoiceNumber = this.invoiceNo;
  this.order.priority = this.priority ; 
  if(status != 'PACKED'){
    this.order.isSelected = this.invoiceDetails.isSelected;
    if(this.invoiceDetails.isSelected){
      this.order.selectList = this.invoiceDetails.selectList; 
    }
  }
  this.auth.updateService('update-invoice', this.order).then((data) => {
    this.auth.closeSpinner();
    this.updateData = data;
    console.log('data:', this.updateData);
    if(this.updateData.code == '0000'){  
      this.helper.presentToast("Order is "+status.toLowerCase() +" successfully");
      if(this.user.roleName == 'USER'){
        this.navCtrl.navigateRoot('/common-dashboard/user-home'); 
      }else {
        this.navCtrl.navigateRoot('/common-dashboard/home');
      }
    }
  }).catch((err) =>{
    this.auth.closeSpinner();
    console.log(err);
  }); 
 }

 

 packed(status:string){  
   this.submitFlag = true; 
   this.order.numOfCases = this.packedStatus.controls['numOfCases'].value; 
   if(this.packedStatus.valid){
    this.updateInv(status);
   }  
  }

  dispatched(status: string){
    this.dispatchButton = true;
    this.dispatchStatus.transporter = this.selectedTransporterId;
    this.dispatchStatus.vehicaleNo = this.transport.controls['vehicaleNo'].value;
    if(this.transport.valid){    
      this.dispatch(); 
    }
  
  }
  //get  transporter
  //http://localhost:8080/rest/api/v1/transport-stockist
  getusers() {
    this.auth.openSpinner();
    this.auth.queryService('transport-stockist').then((data:any) => {
      this.auth.closeSpinner(); 
      this.transporter = data.data;         
      console.log('transporter: '+this.transporter?.transport[0].transportId);      
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    })
  }

  // dropdown for transporter
  transportSelect(e: any) {    
    this.selectedTransporterId = e.detail.value;    
    this.dispatchButton = false;
  }

  invoiceDetails: any;
  invoiceNo: any='' ;
  lineItems: any='' ;
  // get invoice number
  // getInvoiceDetails(ticketId:string){
  //   this.auth.openSpinner();
  //   this.auth.queryService('ticket-order-invoices/'+ticketId).then((data :any) => {
  //     this.auth.closeSpinner();
  //     this.invoiceDetails = data.data;

  //     for(let i=0 ;  i<this.invoiceDetails?.invoices.length; i++){  
  //       if(i==0){
  //         this.invoiceNo = this.invoiceNo +' '+ this.invoiceDetails.invoices[i].invoiceNumber;
  //       this.lineItems = this.lineItems+ ' ' + this.invoiceDetails.invoices[i].lineItem;
  //       }else{
  //         this.invoiceNo = this.invoiceNo +','+ this.invoiceDetails.invoices[i].invoiceNumber;
  //       this.lineItems = this.lineItems+ ',' + this.invoiceDetails.invoices[i].lineItem;
  //       }
  //     }
  //     console.log('invoice details'+this.invoiceNo);
  //     console.log('line Items'+this.lineItems);
  //   }).catch((err) =>{
  //     this.auth.closeSpinner();
  //     console.log(err);
  //   });
  // }


  //'ticket-order-invoice/'+ticketId+'/'+invoice
  //get line items based on invoice

  wms: any;
  pcpd: any;
  invoice = {
    ticketId: this.ticketId,
    invoiceNumber: this.invoiceNo
  }
  mergedInvoice: any;
  getInvoiceDetails(inv: any){
    this.auth.openSpinner();
    this.auth.createService('ticket-order-invoice', inv).then((data :any) => {
          this.auth.closeSpinner();
          this.invoiceDetails = data.result;
          this.wms = data.wms;
          this.mergedInvoice = data?.mergedInvoice;
          this.receivedstatus = this.invoiceDetails.status;
          if(this.user.roleName == 'USER'){
            if(this.receivedstatus == 'INVOICE CREATED' && !this.user.picked ){
              this.helper.invalidOrder('This user is not able to PICK this Order');
              this.router.navigateByUrl('/common-dashboard/user-home'); 
              console.log('picked true');
              this.pcpd = 'pick';              
            } else if(this.receivedstatus == 'PICKED' && !this.user.checked ){
              this.helper.invalidOrder('This user is not able to CHECK this Order');
              this.router.navigateByUrl('/common-dashboard/user-home'); 
              console.log('checked true');
              this.pcpd = 'check';
            } else if(this.receivedstatus == 'CHECKED' && !this.user.packed ){
              this.helper.invalidOrder('This user is not able to PACK this Order');
              this.router.navigateByUrl('/common-dashboard/user-home'); 
              console.log('packed true');
              this.pcpd = 'pack';
            } else if (this.receivedstatus == 'PACKED' && this.type == 'PICKING'  ){               
              this.helper.invalidOrder('invalid QR');
              this.router.navigateByUrl('/common-dashboard/user-home'); 
              
            } else if(this.receivedstatus == 'PACKED' && this.type == 'carton'){
              if(!this.user.dispatched){
                this.helper.invalidOrder('This user is not able to DISPATCH this Order');
                this.router.navigateByUrl('/common-dashboard/user-home');
                console.log('dispatched true');
                this.pcpd = 'dispatch';
              }
            }
             else if(this.receivedstatus == 'DISPATCHED'  ){
              this.helper.invalidOrder('This order is already Dispatched');
            } 
            // else {
            //   this.helper.invalidOrder('This user is not able to process this Order');
            //   this.router.navigateByUrl('/common-dashboard/user-home');            
            // }
          } else if(this.user.roleName == 'MANAGER'){
            if(this.receivedstatus == 'PACKED' && this.type == 'PICKING'){
              this.helper.invalidOrder('invalid QR');
              this.router.navigateByUrl('/common-dashboard/home');
            } 
          } 
          
          if(this.receivedstatus == 'TRANSPORTER ASSIGNED'){
            this.transport.controls['transporter'].patchValue(this.invoiceDetails?.transporter);
            this.transport.controls['vehicaleNo'].patchValue(this.invoiceDetails?.vehicaleNo);
            this.selectedTransporterId = this.invoiceDetails?.transporter;
          }
          
        }).catch((err) =>{  
          this.auth.closeSpinner();
          console.log(err);
        });
  }


  

  viewInvoice(){
  
    this.router.navigate(['/invoice-details'] , {
      queryParams: {
        invoiceNo: this.invoiceDetails.invoiceNumber, 
        ticketNo: this.ticketId
      }
    });

  }

  checkList(){
    this.router.navigate(['/invoice-checking'] , {
      queryParams: {
        invoiceNo: this.invoiceDetails.invoiceNumber, 
        ticketNo: this.ticketId
      }
    });
  }  
}
