import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { HelperService } from 'src/app/services/helper.service';

@Component({
  selector: 'app-invoice-checking',
  templateUrl: './invoice-checking.page.html',
  styleUrls: ['./invoice-checking.page.scss'],
})
export class InvoiceCheckingPage implements OnInit {

  checking: any
  isCheckEnable = false
  invoiceNo: any;
  ticketNo: any;
  form!: FormGroup;
  inputValue: any;
  user: any;

  order = {
    ticketId: '',
    status: '',
    numOfCases: '',
    transporter: '',
    vehicaleNo: '',
    invoiceNumber: '',
    selectList: '',
    isSelected: false,
  }
  invoiceDetails: any;

  constructor(private route: ActivatedRoute, private auth: AuthService, private fb: FormBuilder, private helper: HelperService, private router: Router) {

  }


  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const ticketNo = params['ticketNo'];
      const inv = params['invoiceNo'];
      this.invoiceNo = inv;
      this.ticketNo = ticketNo;
    });
    let u: any = localStorage.getItem('etraze_user');
    this.user = JSON.parse(u);
    this.products();
    let invoice = {
      ticketId: this.ticketNo,
      invoiceNumber: this.invoiceNo
    }
    this.getInvoiceDetails(invoice);
  }

  backNav() {
    if(this.user.roleName == 'USER'){
      this.router.navigateByUrl('/common-dashboard/user-home');
    } else if(this.user.roleName == 'MANAGER'){
      this.router.navigateByUrl('/common-dashboard/home');
    }
  }

  productList: any = [];
  product: any;
  invoice: any = [];

  products() {
    this.invoice.push(this.invoiceNo);
    this.auth.createService('invoice-detail',this.invoice).then((data) => {
      this.productList = data;
      this.checkButtonEnable();
    }).catch((err) => {
      console.log(err);
    });
  }

  tenantName: any;
  getInvoiceDetails(inv: any){
    this.auth.createService('ticket-order-invoice',inv).then((data : any) => {
      this.tenantName = data?.tenantName;
      this.invoiceDetails = data.result;
    }).catch((err) => {
      console.log(err);
    });
  }

  input(event: any, item: any, idx: number) {
    console.log(item);
    console.log(event.target.value);
    console.log(typeof item.quantity , typeof event.target.value);

    var itemQuantity = parseInt(item.quantity);
    var eventValue = parseInt(event.target.value);

    if (!isNaN(itemQuantity) && !isNaN(eventValue)) {
      if (itemQuantity < eventValue || itemQuantity > eventValue) {
          this.helper.presentErrorToast('Quantity mismatch');
      }
    }
    this.inputValue = event.target.value;
    item.pickItems = this.inputValue;
    this.quantity(item);
    this.checkButtonEnable();
  }

  checkButtonEnable() {
    for (const item of this.productList) {
      if (item.pickItems === undefined || item.pickItems === null || item.pickItems === "" || item.pickItems === "0" ||item.pickItems != item.quantity) {
        this.isCheckEnable = false;
        return;
      } else {
        this.isCheckEnable = true;
      }
    }
  }

  quantity(ser: any) {
    this.auth.updateService('invoice-line-items', ser).then((data) => {
      console.log(data);
    }).catch((err) => {
      console.log(err);
    });
  }

  check(status: any) {
    this.order.ticketId = this.ticketNo;
    this.order.invoiceNumber = this.invoiceNo;
    this.order.status = status;
    this.order.isSelected = this.invoiceDetails.isSelected;
      if(this.invoiceDetails.isSelected){
        this.order.selectList = this.invoiceDetails.selectList;
      }
    console.log(this.order);
    this.auth.updateService('update-invoice', this.order).then((data) => {
      console.log(data);
      this.checking = data;
      if (this.checking.code == '0000') {
        if(this.user.roleName == 'USER'){
          this.router.navigateByUrl('/common-dashboard/user-home');
          this.helper.presentToast('Invoice checked successfully');
        } else if (this.user.roleName == 'MANAGER'){
          this.router.navigateByUrl('/common-dashboard/home');
          this.helper.presentToast('Invoice checked successfully');
        }
      }
    }).catch((err) => {
      console.log(err);
    });
  }
}
