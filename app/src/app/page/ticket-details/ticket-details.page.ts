import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NavController, Platform } from '@ionic/angular';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-ticket-details',
  templateUrl: './ticket-details.page.html',
  styleUrls: ['./ticket-details.page.scss'],
})
export class TicketDetailsPage implements OnInit {

  backButtonSubscription: any;
  status = false;
  ticketDetail: any;
  ticketId: any;
  getTicketDetails: any;

  constructor(
    public platform:Platform, 
    private navCtrl: NavController , 
    private router: Router,
    private auth: AuthService,
    private route: ActivatedRoute
    ) { }

  ngOnInit() {
    this.ticketId = this.route.snapshot.paramMap.get('ticketNo');
    this.getTicketDetail();
    this.platform.backButton.subscribeWithPriority(9999, () => {
      console.log("BACK003");
      this.backNav();
    });
    // this.route.params.subscribe((params: any) => {
    //   this.ticketId = params["ticketId"];
    //   this.getTicketDetails(this.ticketId);
    //   console.log(this.ticketId);
    // });

  }

  viewStatus(){
    if(this.status){
      this.status = false;
    }else{
      this.status = true;
    } 
  }

   ngOnDestroy() {
    // this.backButtonSubscription.unsubscribe();
    console.log('onDestroy');
   }

  backNav(){
    this.navCtrl.pop();
    this.router.navigateByUrl('/common-dashboard/tickets');    

  }

  public counts = ["Email Received on ","Invoice Created","Picked","Checked","Packed","Dispatched","Deliverd"];
  public orderStatus = "In Progress"

  getTicketDetail() {
    this.auth.openSpinner();
    this.auth.queryService('ticket-details/'+this.ticketId).then((data) => {
      this.auth.closeSpinner(); 
      this.ticketDetail = data;
      // console.log(data);
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    })
  }
}
