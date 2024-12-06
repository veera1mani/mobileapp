import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-dashboard-header',
  templateUrl: './dashboard-header.component.html',
  styleUrls: ['./dashboard-header.component.scss'],
})
export class DashboardHeaderComponent implements OnInit {

  logoutModel = false;

  constructor(private router: Router, private navctrl: NavController, private auth: AuthService) { }

  ngOnInit() {
    this.logoutModel = false;
  }



  openModel() {
    this.logoutModel = true;
  }

  closeModel() {
    this.logoutModel = false;
  }


  /**
   * 
   */

  logout() {
    this.auth.openSpinner();
    this.auth.logout().then((data: any) => {
      this.auth.closeSpinner(); 
      this.logoutModel = false;
      if(data.message == 'Token required'){
        const tenant: any = localStorage.getItem('tenant_id');
        if(localStorage.clear() == null ){
          localStorage.setItem('tenant_id', tenant);
        }
        this.navctrl.navigateRoot("/enter-pin");
      }
      const tenant: any = localStorage.getItem('tenant_id');
      if(localStorage.clear() == null ){
        localStorage.setItem('tenant_id', tenant);
      }
      this.navctrl.navigateRoot("/enter-pin");
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err)
    })
  }


  /**
   * 
   */
  openNotification() {
    this.router.navigateByUrl("/etraze-notification");
  }
}
