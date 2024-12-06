import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-owner-orders',
  templateUrl: './owner-orders.page.html',
  styleUrls: ['./owner-orders.page.scss'],
})
export class OwnerOrdersPage implements OnInit {

  totalEmailReceived:number=777;
  totalOrders:number=456;
  totalTickets:number=555;
  totalUnassigned:number=987;
  
  userNames:string[]=['Veera','priya','Vasanth','Bala'];
  
  emailReceivedNumbers:number[]=[22,33,44,55];
  orders:number[]=[45,77,89,43];
  tickets:number[]=[65,78,54,32];
  unassigned:number[]=[98,78,54,32];
  
  isMonthly: boolean = true; 
  
  
    selectedView:string='monthly';
    selectedSegment: string = 'monthly';  /* for starting month from may */
  
    /* for perfomance and monthy*/
    selectedToggle:string='perfomance';
  
    toggleView(view :string){
      this.selectedView=view;
    }
  
    toggleToggle(toggle : string){
      this.selectedToggle=toggle;
    }
  
   
   
    selectedMonths:{ [key:string]:boolean}={};
  
      toggleMonth(month: string): void {
      this.selectedMonths[month] = !this.selectedMonths[month];
      }
  
  
    /* for Months display*/
  
  
  isMobileView(): boolean {
    
    return window.innerWidth < 668; 
  }
  
  isDisplayMonth(month: string): boolean {
    const mobileMonths = ['May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov'];
    const formattedMonth = month.slice(0, 3); // Get the first three letters of the month
    return this.isMobileView() && mobileMonths.includes(formattedMonth);
  }
  
  months:any[]=[
    {name:'Jan',id:1,fillcolor:false},
    {name:'Feb',id:2,fillcolor:false},
    {name:'Mar',id:3,fillcolor:false},
    {name:'Apr',id:4,fillcolor:false},
    {name:'May',id:5,fillcolor:false},
    {name:'Jun',id:6,fillcolor:false},
    {name:'Jul',id:7,fillcolor:false},
    {name:'Aug',id:8,fillcolor:false},
    {name:'Sep',id:9,fillcolor:false},
    {name:'Oct',id:10,fillcolor:false},
    {name:'Nov',id:11,fillcolor:false},
    {name:'Dec',id:12,fillcolor:false},
  ];
  
  
  selectedMonth(selected: any){    
    console.log(selected.name);
    this.months.forEach((obj:any) => {
     if(obj.id == selected.id){
        obj.fillcolor = true;
     }
     else{
       obj.fillcolor = false;
     }
    });
   }
  
    constructor() { }
  
    ngOnInit() {
      console.log('init');
    }

}
