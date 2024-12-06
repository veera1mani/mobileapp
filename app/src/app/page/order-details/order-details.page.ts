import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NavController, Platform } from '@ionic/angular';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.page.html',
  styleUrls: ['./order-details.page.scss'],
})
export class OrderDetailsPage implements OnInit {
  backButtonSubscription: Subscription = new Subscription;
  status = false;
  ticketId: any;
  orderDetail: any;
  invoiceDetails: any;
  invoiceNo: any;
  numOfCases: any;
  lineItems: any;
  inv: any;

  constructor(
    public platform:Platform,
    private navCtrl: NavController , 
    private router: Router,
    private auth: AuthService,
    private route: ActivatedRoute,
    ) { }

  ngOnInit() {
    // this.ticketId = this.route.snapshot.paramMap.get('ticketNo');
    this.route.queryParams.subscribe(params => {
      this.ticketId = params['ticketNo'],
      this.inv = params['invoiceNo']
    });
    let invoice = {
      ticketId: this.ticketId,
      invoiceNumber: this.inv
    }
    this.getOrderDetail();
    this.getInvoiceDetails(invoice);
  }

  ionViewWillEnter(){
    this.backButtonSubscription = this.platform.backButton.subscribeWithPriority(9999, () => {
      this.backNav();
    });
  }
  ionViewWillLeave(){
    this.backButtonSubscription.unsubscribe();
  }

  viewStatus(){
    if(this.status){
      this.status = false;
    }else{
      this.status = true;
    }
  }

   ngOnDestroy() {
    console.log('onDestroy');
   }

  backNav(){
    this.router.navigateByUrl('/common-dashboard/orders');    

  }

  public counts = ["Email Received on ","Invoice Created","Picked","Checked","Packed","Dispatched","Deliverd"];
  public orderStatus = "In Progress" 

  getOrderDetail() {
    this.auth.openSpinner();
    this.auth.queryService('ticket-details/'+this.ticketId).then((data: any) => {
      this.auth.closeSpinner(); 
      this.orderDetail = data.data;
      this.invoiceDetails = data.data.orderDTO ;
      for(let i=0 ;  i<this.invoiceDetails?.invoices.length; i++){
        if(i==0){
          this.invoiceNo =  this.invoiceDetails.invoices[i].invoiceNumber;
          this.lineItems =  this.invoiceDetails.invoices[i].lineItem;
        }else{
          this.invoiceNo = this.invoiceNo +','+ this.invoiceDetails.invoices[i].invoiceNumber;
          this.lineItems = this.lineItems +','+ this.invoiceDetails.invoices[i].lineItem;
        }       
      }
      console.log(data);
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    });
  }
  
  invoise: any;
  getInvoiceDetails(ser: any){
    this.auth.openSpinner();
    this.auth.createService('ticket-order-invoice', ser).then((data: any) => {
      this.auth.closeSpinner();
      this.invoise = data.result;
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    });
  }
}
