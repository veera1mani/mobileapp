import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-owner-dispatch',
  templateUrl: './owner-dispatch.page.html',
  styleUrls: ['./owner-dispatch.page.scss'],
})
export class OwnerDispatchPage implements OnInit {

  userNames:string[]=['ABT','City','GRK','NBB','PSS'];
  dispatches:number[]=[45,77,89,43,56];
  cases:number[]=[65,78,54,32,73];
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
  constructor() { }

  isMonthly: boolean = true;

  ngOnInit() {
    console.log('init');
  }

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
}
