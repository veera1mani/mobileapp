import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NavController } from '@ionic/angular';
import { AuthService } from 'src/app/services/auth.service';
import { HelperService } from 'src/app/services/helper.service';



@Component({
  selector: 'app-invoice-details',
  templateUrl: './invoice-details.page.html',
  styleUrls: ['./invoice-details.page.scss'],
})


export class InvoiceDetailsPage implements OnInit {


  invoiceNo: any ;
  ticketNo: any;
  size: any;
  invoiceDetails: any ;
  user: any;

  constructor(private router: Router, private route: ActivatedRoute, private helper: HelperService,public navCtrl: NavController,
    public navParams: ActivatedRoute, public auth: AuthService) 
  {
   
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

  backNav(){
    if(this.user.roleName == 'USER'){
      this.navCtrl.navigateRoot('/common-dashboard/user-home');
    } else {
      this.navCtrl.navigateRoot('/common-dashboard/home');
    }
  }

  tenantName : any;
  getInvoiceDetails(inv: any){
    this.auth.createService('ticket-order-invoice', inv).then((data : any) => {
      this.tenantName = data?.tenantName;
      this.invoiceDetails = data.result;
      console.log(this.invoiceDetails);      
    }).catch((err) => {
      console.log(err);
    });
  }

  
  
  selectedValues: any;
  selectedItems: string = '0';

  lineItem ={
    id: '',
    invoiceId: '',
    productCode: '',    
    productName:'',
    picked: 'false',
    checked: 'true',
    status: '',
  }
  

  checkboxChanged(event: any,checkboxChanged:any) {
    this.selectItem();     
    this.lineItem.picked = event.detail.checked;    
    this.update(checkboxChanged);
  }

  // partialPick: boolean = false;
  // partialCheck: boolean = false;
  // location : any;
  selectItem(){
    this.selectedValues = this.productList.filter((item: { checked: any; }) => item.checked).map((item: { name: any; }) => item.name);
    this.selectedItems = this.selectedValues.length ; 

    
    // const currentLocationMap: { [location: string]: boolean } = {};

    // // Track the current status of products by their location
    // this.productList.forEach((item: { location: string; checked: boolean }) => {
    //   if (!currentLocationMap[item.location]) {
    //     currentLocationMap[item.location] = item.checked;
    //   } else {
    //     currentLocationMap[item.location] = currentLocationMap[item.location] && item.checked;
    //   }
    // });
  
    // const locationMap = this.separateByLocation(this.pickingProductList);
    // console.log('location map ::::::: ',locationMap);

    // // Notify if all products in a location are picked
    // Object.keys(locationMap).forEach(location => {
    //   const allPicked = locationMap[location].every((item: { checked: boolean }) => item.checked);
    //   if (allPicked && this.selectedItems != this.pickingProductList.length) {
    //     this.partialPick = true;
    //     this.location =  location ;
    //     this.helper.presentToast(`all the products in ${location} picked`);
    //   }
    // });
  }
  // locationMap: any;
  // separateByLocation(productList: any[]): { [location: string]: any[] } {
  //   const locationMap: { [location: string]: any[] } = {};
  //   productList.forEach((item: { location: string }) => {
      
  //     if (!locationMap[item.location]) {
  //       locationMap[item.location] = [];
  //     }
  //     locationMap[item.location].push(item);
  //   });
  
  //   return locationMap;
  // }

  update(ser: any){
    this.auth.updateService('invoice-line-items',ser).then((data)=>{
      console.log(data);
    }).catch((err)=>{
      console.log(err);
    });
  }

  order ={
    ticketId: '',
    status: '',    
    numOfCases:'',
    transporter: '',
    vehicaleNo: '',
    invoiceNumber: '',
    selectList: '',
    isSelected: false,
  }

  res: any;
  picked(status: any) {
    this.order.invoiceNumber = this.invoiceNo;
    this.order.status = status;
    this.order.ticketId = this.ticketNo;   
      this.order.isSelected = this.invoiceDetails.isSelected;
      if(this.invoiceDetails.isSelected){
        this.order.selectList = this.invoiceDetails.selectList; 
      }
      this.auth.updateService('update-invoice', this.order).then((data) => {

    console.log(data);
    this.res = data;
    if(this.res.code == '0000') {
      if(this.user.roleName == 'USER'){
        this.router.navigateByUrl('/common-dashboard/user-home');
        this.helper.presentToast('Invoice picked successfully');
      } else if (this.user.roleName == 'MANAGER'){
        this.router.navigateByUrl('/common-dashboard/home');
        this.helper.presentToast('Invoice picked successfully');      
      }
    }
    
   }).catch((err) => {
    console.log(err);
   });
  }


  

  alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');
  sorted_users: any  = {  } ;
  groups :any = [];
  shownGroup = null;

  public tmp: any = [];
  

  initializeData() {
    // Sort user list by first letter of name
    // for (let i = 0; i < this.items.length; i++) {
    //   const letter = this.items[i].name.toUpperCase().charAt(0);
    //   if (this.tmp[letter] == undefined) {
    //     this.tmp[letter] = [];
    //   }
    //   this.tmp[letter].push(this.items[i]);
    // }
    this.productList = this.tmp;

    // Create groups
    for (let i = 0; i < 10; i++) {
      this.groups[i] = {
        name: i,
        items: []
      };
      for (let j = 0; j < 3; j++) {
        this.groups[i].items.push(`${i}-${j}`);
      }
    }
  }

 gotoList(listId: string): void {
    const listElement = document.getElementById(listId);
    if (listElement) {
      listElement.scrollIntoView({ behavior: 'smooth' });
    }
 }

 /**
  * get invoice details
  */
 productList: any=[];
 product: any;
 invoice : any  =[ ];
//  pickingProductList : any = [];
//  checkingProductList: any =[];
//  groupedCheckingProducts: any = [];
//  groupedLocation: any = [];

 products(){
    this.invoice.push(this.invoiceNo);
    // this.pickingProductList  = [];
    // this.checkingProductList =[];
    this.auth.createService('invoice-detail',this.invoice).then((data) => {
    this.productList = data; 
    // this.productList.forEach((product: any) => {
    //   if(product.partialPick  ){
    //     this.checkingProductList.push(product);
    //   }else{
    //     this.pickingProductList.push(product);
    //   }
    // });
    // if(this.checkingProductList.length > 0 ){
    //   this.groupedCheckingProducts = this.separateByLocation(this.checkingProductList);
    //   this.groupedLocation = this.getLocationKeys(this.groupedCheckingProducts);
    //   this.groupedLocation.forEach((element:any) => {
    //     this.checkEveryProductsChecked(element);
    //   });
    // }
    this.selectItem();     
  }).catch((err) => {
    console.log(err);
  });
 }

 toggleChecked(item: any){
  item.checked = !item.checked;
  console.log('item ',item.checked);
  this.selectItem();     
  this.lineItem.picked = item.checked;    
  this.update(item);
 }
//  partialProducts: any = [];
//  partialSave(){    
//   this.pickingProductList.forEach((element: any) => {
//     if(element.location == this.location){
//       this.partialProducts.push(element); 
//     }  
//   });
//   this.save('PICKED',this.partialProducts);
 
//  }

//  save(status: string ,ser: any){
//   this.auth.openSpinner();
//   this.auth.updateService('partial-pick?status='+status,ser).then((data: any) =>{
//     this.auth.closeSpinner();
//     console.log(data);
//     if(data.code === '0000'){
//       this.products();  
//       this.helper.presentToast(`${this.location} , ${this.partialProducts.length} items partially picked `);
//       this.partialPick = false;
//       this.location =  null ;
//       this.partialProducts = [];  
//     }else{
//       this.helper.presentErrorToast('items are not able to partially picked');
//     }
//   }).catch((err) =>{
//     console.log(err);
//     this.auth.closeSpinner();
//   }); 
//  }

//  openAccordions: { [location: string]: boolean } = {};

//  toggleAccordion(location: string) {
//    this.openAccordions[location] = !this.openAccordions[location];
//  }

//  isAccordionOpen(location: string): boolean {
//    return !!this.openAccordions[location];
//  }

//  getLocationKeys(map: any) {  
//   return Object.keys(map);
//   }

//   inputValue: any ;
//   input(event: any, item: any, idx: number) {
//     var itemQuantity = parseInt(item.quantity);
//     var eventValue = parseInt(event.target.value);
//     if (!isNaN(itemQuantity) && !isNaN(eventValue)) {
//       if (itemQuantity < eventValue || itemQuantity > eventValue) {
//           this.helper.presentErrorToast('Quantity mismatch');
//       }
//     }
//     this.inputValue = event.target.value;
//     item.pickItems = this.inputValue;
//     this.quantity(item);
//     this.checkEveryProductsChecked(item.location);
//   }

//   quantity(ser: any) {
//     this.auth.updateService('invoice-line-items', ser).then((data) => {
//       console.log(data);
//     }).catch((err) => {
//       console.log(err);
//     });
//   }

//   partialCheckSave(items: any){
//     console.log('partial check clicked ',items);
//     this.save('CHECKED',items);
//   }

//   checkEveryProductsChecked(loc: string){
//     console.log(loc);
//     const allChecked = this.groupedCheckingProducts[loc].every((item: { pickItems : string , quantity: string}) => item.pickItems != "" && item.pickItems == item.quantity);
//       if (allChecked  ) {
//         this.partialCheck = true;
//         this.helper.presentToast(`all the products in ${loc} Checked`);
//       }
//   }

}
