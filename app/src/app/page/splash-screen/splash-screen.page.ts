import { Component, OnInit } from '@angular/core';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-splash-screen',
  templateUrl: './splash-screen.page.html',
  styleUrls: ['./splash-screen.page.scss'],
})
export class SplashScreenPage implements OnInit {

  userRole: any;
  tenantId: any;

  constructor(public navCtrl: NavController) { }

  ngOnInit() {
  setTimeout(() => { 
        // this.navigateToUrl(); 
        // this.tenantId = localStorage.getItem('tenant_id');
        // if(this.tenantId){
        //   this.navCtrl.navigateRoot('/enter-pin');
        // }else {
        //   this.navCtrl.navigateRoot('/login-page');
        // }
        this.navCtrl.navigateRoot('/scan-qr');
     }, 2000);    
    
  }

  
}
