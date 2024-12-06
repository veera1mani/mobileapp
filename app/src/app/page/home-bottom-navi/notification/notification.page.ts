import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NavController, Platform } from '@ionic/angular';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.page.html',
  styleUrls: ['./notification.page.scss'],
})
export class NotificationPage implements OnInit, OnDestroy {

  notifications: any;
  userRole  : any;
  backButtonSubscription: Subscription = new Subscription;

  constructor(private router: Router,private platform: Platform, private navctrl: NavController, private auth: AuthService) { }

  ngOnInit() {
    this.userRole = localStorage.getItem('user_role');
    this.getMyNotification();
  }

  ngOnDestroy(): void {
    console.log('destroyu');
  }
  
  backNav() {
    this.navctrl.pop();
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
