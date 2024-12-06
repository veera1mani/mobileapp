import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NavController, Platform } from '@ionic/angular';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-etraze-notification',
  templateUrl: './etraze-notification.page.html',
  styleUrls: ['./etraze-notification.page.scss'],
})
export class EtrazeNotificationPage implements OnInit, OnDestroy {

  notifications: any;
  userRole:any;
  backbtnSubscription: Subscription = new Subscription;

  constructor(private router: Router, private navCtrl: NavController , private platform: Platform , private navctrl: NavController, private auth: AuthService) { }

  ngOnInit() {
    this.getMyNotification();
    this.userRole = localStorage.getItem("user_role");
  }

  ionViewWillEnter(){
    this.backbtnSubscription = this.platform.backButton.subscribeWithPriority(19 , () =>{
      console.log('back buttton 20');
      this.backNav();
    })
  }
  ionViewWillLeave(){
    this.backbtnSubscription.unsubscribe();
  }

  ngOnDestroy(): void {
    console.log('destroy');
  }
  backNav() {
    
    if(this.userRole == "MANAGER"  ){
      this.router.navigateByUrl('/common-dashboard/home');
    }
    else if(this.userRole == "USER"){
      this.router.navigateByUrl('/common-dashboard/user-home');
    }
    else if(this.userRole == "SUPERADMIN"){
      this.router.navigateByUrl('/common-dashboard/o-orders');
    }
    else if(this.userRole == "SECURITY"){
      this.router.navigateByUrl('/visitor');
    }
  }


  getMyNotification() {
    this.auth.openSpinner();
    this.auth.queryService('mynotifications').then((data) => {
      this.auth.closeSpinner();
      this.notifications = data;
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    })
  }
}
