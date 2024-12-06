import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-role',
  templateUrl: './role.page.html',
  styleUrls: ['./role.page.scss'],
  providers:[AuthService]
})
export class RolePage implements OnInit {

  receivedstatus: any;
  users: any;
  ticketdetails: any;
  ticketId!:string;
  updateData: any;

  constructor(private router: Router, private auth: AuthService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.subscribe((params: any) => {
      this.ticketId = params["ticketId"];
      this.getOrderDetails(this.ticketId);
      console.log(this.ticketId);
    });
  }

  id: any = localStorage.getItem("etraze_ticketid");

  backNav(){
    // this.navCtrl.pop();
    this.router.navigateByUrl('/common-dashboard/user-home');    

  }


  // after scan api call for get a order

  orderDetails:any;
  getOrderDetails(ticketId:string){
    this.auth.openSpinner();
    this.auth.queryService('ticket/'+ticketId).then((data) => {
      this.auth.closeSpinner();
      this.orderDetails = data;
      console.log(this.orderDetails);
      this.receivedstatus = this.orderDetails.status;
    }).catch((err) =>{
      this.auth.closeSpinner();
      console.log(err);
    })
  }


 // update status of order
 updateOrderStatus(status:string){
  this.auth.openSpinner();
  this.auth.updateService('update-ticket', {ticketId:this.ticketId, status:status}).then((data) => {
    this.auth.closeSpinner();
    this.updateData = data;
    console.log("this: " + data);
    if(this.updateData.code == '0000'){
      this.router.navigateByUrl('/common-dashboard/user-home')
    }
  }).catch((err) =>{
    this.auth.closeSpinner();
    console.log(err);
  })
}
  
}
