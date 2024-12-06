import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NavController, Platform } from '@ionic/angular';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-return-details',
  templateUrl: './return-details.page.html',
  styleUrls: ['./return-details.page.scss'],
})
export class ReturnDetailsPage implements OnInit {

  backButtonSubscription: any;
  status = false;
  returnId: any;
 returnDetail: any;


  constructor(public platform:Platform,
    private auth: AuthService,
    private navCtrl: NavController , private router: Router,private route: ActivatedRoute) { }

  ngOnInit() {
    this.returnId = this.route.snapshot.paramMap.get('returnNo');
    this.getReturnDetail();
    this.platform.backButton.subscribeWithPriority(9999, () => {
      console.log("BACK002");
      this.backNav();
    });
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
    // this.navCtrl.pop();
    this.router.navigateByUrl('/common-dashboard/returns');    

  }

  public counts = ["Email Received on ","Invoice Created","Picked","Checked","Packed","Dispatched","Deliverd"];
  public orderStatus = "In Progress"

  getReturnDetail() {
    this.auth.openSpinner();
    this.auth.queryService('return-details/'+this.returnId).then((data) => {
      this.auth.closeSpinner(); 
      this.returnDetail = data;
      console.log(data);
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    })
  }
}
