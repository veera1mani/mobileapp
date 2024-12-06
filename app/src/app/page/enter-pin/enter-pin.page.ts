import { Component, OnInit ,OnDestroy} from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { HelperService } from 'src/app/services/helper.service';
import { Device } from '@capacitor/device';
import { NavController , Platform} from '@ionic/angular';
import jsPDF from 'jspdf';
import { App } from '@capacitor/app';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
// import jsPDF from '../../../assets/scanner.jpg';




@Component({
  selector: 'app-enter-pin',
  templateUrl: './enter-pin.page.html',
  styleUrls: ['./enter-pin.page.scss'],
})
export class EnterPinPage implements OnInit,OnDestroy {

  enteredPin: any;
  user: any;
  device: any;
  data: any;
  users: any;
  tenant: any;
  signUp = "don't have an account? Create Now";
  private lastBackTime = 0;
  backButtonSubscription: Subscription = new Subscription; 

  constructor(private auth: AuthService, private platform: Platform,private router: Router, private helper: HelperService, private navCtrl: NavController) { }

  ngOnInit() {
    console.log('init stage');
    let u: any = localStorage.getItem('etraze_user');
    this.device = localStorage.getItem('device_id');
    this.tenant = localStorage.getItem('tenant_id'); 
    this.user = JSON.parse(u);      
  }

  ionViewWillEnter(){
   this.backButtonSubscription = this.platform.backButton.subscribeWithPriority(11, () => {
      console.log('back button 2');
      this.handleBackButton();
    });
  }

  ionViewWillLeave(){
    this.backButtonSubscription.unsubscribe();
  }

  private handleBackButton() {
    const now = new Date().getTime();
    const timeDiff = now - this.lastBackTime;

    if (timeDiff < 2000) { 
      App.exitApp();
    } else {
      this.helper.exit1('tap again to exit');
      this.lastBackTime = now;
    }    
  }

  ngOnDestroy(): void {
    console.log('inside destroy');
  }

  backNav(){

  }

  onCodeChanged(code: string) { }
  
  onCodeCompleted(code: string) {
    this.enteredPin = code;
  }
  

  // submit(){
  //   this.auth.pinLogin("pin-login?pin="+this.enteredPin+"&tenantId="+this.tenant).then((data: any) =>{
      
  //     this.data = data.data;
  //     let id : any = atob(this.data.uid);      
  //     let pass: any = atob(this.data.pass);      
  //     const usercredential: any = btoa(id +':'+pass);
      
  //     if(data.code == '0000'){
        
  //       this.auth.openSpinner();
        
  //       this.auth.login(usercredential).then((data) => {
  //         if(data.code == '0000'){
  //           this.users = data.data;
  //           localStorage.setItem('etraze_role', JSON.stringify(this.users.role));
  //           localStorage.setItem('etraze_user', JSON.stringify(this.users.user));
  //           localStorage.setItem('etraze_token', this.users.token);
  //           localStorage.setItem('user_role', this.users.role.roleName);
  //           localStorage.setItem('user_name', this.users.user.firstName); 
  //           localStorage.setItem('last_name',this.users.user.lastName);
  //           localStorage.setItem('tenant_id', this.users.user.tenantId);
  //           if(this.users.role.roleName === 'MANAGER'){
  //               this.helper.presentErrorToast('Manager does not not login to mobile');
  //             } else if(this.users.role.roleName == "STOCKIST"){
  //               this.navCtrl.navigateRoot('/common-dashboard/home');
  //             }
  //             else if(this.users.role.roleName == "SUPERADMIN"){
  //               this.navCtrl.navigateRoot('/common-dashboard/o-orders');
  //             }
  //             else if(this.users.role.roleName == "USER"){
  //               this.navCtrl.navigateRoot('/common-dashboard/user-home');
  //             }
  //             else if(this.users.role.roleName == "TRANSPORT"){
  //               this.navCtrl.navigateRoot('/common-dashboard/transporter');
  //             }
  //             else{
  //               this.helper.presentErrorToast('this user is not available for mobile');     
  //             }
  //             this.helper.presentToast('logged in successfully...');
  //         }
         
  //       }).catch((err)=>{
  //         this.helper.presentErrorToast('invalid user');
  //       });         
  //     }
  //   }).catch((err)=>{ 
  //     this.helper.presentErrorToast('invalid user');
  //   })  
  // }

  pin(){
    this.auth.openSpinner();
    const usercredential: any = btoa(this.enteredPin+ ":" + this.tenant);
    this.auth.loginPin(usercredential).then((data: any ) => {
      this.auth.closeSpinner();
      if(data.code == '0000'){
        this.auth.closeSpinner();
        this.users = data.data;
        localStorage.setItem('etraze_role', JSON.stringify(this.users.role));
        localStorage.setItem('etraze_user', JSON.stringify(this.users.user));
        localStorage.setItem('etraze_token', this.users.token);
        localStorage.setItem('user_role', this.users.role.roleName);
        localStorage.setItem('user_name', this.users.user.firstName); 
        localStorage.setItem('last_name',this.users.user.lastName);
        localStorage.setItem('tenant_id', this.users.user.tenantId);
        if(this.users.role.roleName === 'STOCKIST'){
            this.helper.presentErrorToast('Invalid user');
          } else if(this.users.role.roleName == "MANAGER"){
            this.navCtrl.navigateRoot('/common-dashboard/home');
          }
          else if(this.users.role.roleName == "SUPERADMIN"){
            this.navCtrl.navigateRoot('/common-dashboard/o-orders');
          }
          else if(this.users.role.roleName == "USER"){
            this.navCtrl.navigateRoot('/common-dashboard/user-home');
          }
          else if(this.users.role.roleName == "TRANSPORT"){
            this.navCtrl.navigateRoot('/common-dashboard/transporter');
          }
          else if(this.users.role.roleName === 'SECURITY'){
            this.navCtrl.navigateRoot('/visitor');                  
          }
          else{
            this.helper.presentErrorToast('this user is not available for mobile');     
          }
          this.helper.presentToast('logged in successfully...');
      } else {
        this.auth.closeSpinner();
        this.helper.presentErrorToast('invalid user');
      }
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    });
  }

  Login(){
    this.router.navigateByUrl('/login-page');
  } 

 
}
