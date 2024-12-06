import { Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PluginListenerHandle } from '@capacitor/core';
import { NavController, Platform, ToastController } from '@ionic/angular';
import { AuthService } from 'src/app/services/auth.service';
import { HelperService } from 'src/app/services/helper.service';
import { Device } from '@capacitor/device';
import { App } from '@capacitor/app';
import { Subscription } from 'rxjs';
import { Camera} from '@capacitor/camera';
import { Geolocation } from '@capacitor/geolocation';




@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  providers:[AuthService]

})
export class LoginPage implements OnInit, OnDestroy {

  loginForm: FormGroup;
  users: any = [];
  passwordtext: boolean = false;
  userRole: any;
  submitFlag: boolean = false;
  PAT_EMAIL = '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,7}$';

  status: any;
  private lastBackTime = 0;
  backButtonSubscription: Subscription = new Subscription; 

  


  constructor(private helper:HelperService, private platform: Platform ,  private router: Router, private fb: FormBuilder,private auth: AuthService,  public navCtrl: NavController, private ngZone: NgZone , private toastController: ToastController) {   
    this.loginForm = this.fb.group({
      username: [null, [Validators.required, Validators.pattern(this.PAT_EMAIL)]],
      password: [null, Validators.required],            
    });
  }

   ngOnInit() {
    console.log('init'); 
    Camera.requestPermissions({permissions: ['camera']});       
  }

 

  ionViewWillEnter(){
    this.backButtonSubscription = this.platform.backButton.subscribeWithPriority(10, () => {
      console.log("BACK001");
      this.handleBackButton();
    });
  } 

  ionViewWillLeave(){
    this.backButtonSubscription.unsubscribe();
  } 

  ngOnDestroy(): void {
    console.log('inside on destroy');
  }

  private handleBackButton() {
    const now = new Date().getTime();
    const timeDiff = now - this.lastBackTime;

    if (timeDiff < 2000) { 
      App.exitApp();
    } else {
      this.helper.exit('tap again to exit');
      this.lastBackTime = now;
    }    
  }

  
  

  changeStatus(status: any){
    this.status = status?.connected;
    
    if(!this.status){     
      this.helper.presentNetworkToast(" Seems like you are offline...");
    }      
    else{ 
      console.log('inside else present alert');
    }
  }

                                                        
  pass(){   
    if(this.passwordtext===false){
      this.passwordtext=true;
    } else{
      this.passwordtext=false;
    }      
  }
 
  tenantId: any;

  login() {
    this.loginForm.markAllAsTouched();
    this.submitFlag= true;
    const usercredential: any = btoa(this.loginForm.value.username.toUpperCase() + ":" + this.loginForm.value.password);
    
    this.auth.openSpinner();
    this.auth.login(usercredential).then((data: any) => {
      this.auth.closeSpinner();     
      this.users = data.data;
      this.tenantId = localStorage.getItem('tenant_id');
      
      if(this.tenantId == null || this.tenantId == this.users.user.tenantId){      
          if (data.code == "0000") {
            this.auth.closeSpinner();
            localStorage.setItem('etraze_role', JSON.stringify(this.users.role));
            localStorage.setItem('etraze_user', JSON.stringify(this.users.user));
            localStorage.setItem('etraze_token', this.users.token);
            localStorage.setItem('user_role', this.users.role.roleName);
            localStorage.setItem('user_name', this.users.user.firstName); 
            localStorage.setItem('last_name',this.users.user.lastName);
            localStorage.setItem('tenant_id', this.users.user.tenantId);

            this.auth.pinAvailable().then((data) => {
              if(data.code == '0000'){
                this.helper.presentToast('Logged in successfully...');
                
                if(this.users.role.roleName === 'TRANSPORT'){
                  this.navCtrl.navigateRoot(['/common-dashboard/transporter']);           
                } else if(this.users.role.roleName === 'MANAGER'){
                  this.navCtrl.navigateRoot('/common-dashboard/home');
                } else if(this.users.role.roleName === 'SUPERADMIN'){
                  this.navCtrl.navigateRoot('/common-dashboard/o-orders');                      
                } else if(this.users.role.roleName === 'USER'){
                  this.navCtrl.navigateRoot('/common-dashboard/user-home');                  
                }else if(this.users.role.roleName === 'SECURITY'){
                  this.navCtrl.navigateRoot('/visitor');                  
                }
                 else {
                  this.helper.presentErrorToast(this.users.user.role +" is not available for mobile");
                }        
              } else{                    
                  if(this.users.role.roleName === 'STOCKIST'){
                    this.helper.presentErrorToast("invalid user");            
                  } 
                  else{
                    this.helper.presentToast('Logged in successfully...');
                    this.navCtrl.navigateRoot('/set-pin');
                  }
              }
            }).catch((err) => {
              console.log('error',err);
            });        
            
          } else {
            this.auth.closeSpinner();
            this.helper.presentErrorToast("Please enter valid email");
          }

      } else {
        this.auth.closeSpinner();
        this.helper.presentErrorToast("this user does not belongs to this tenant");
      }
    }).catch((err) => { 
      this.auth.closeSpinner();
      this.helper.presentErrorToast("Please enter valid credentials");
      console.log("err", err);      
    });   
  }   

  logDeviceId = async () => {
    const info = await Device.getId();
  }
 
  enterPin(){
    this.router.navigateByUrl('/enter-pin');
  } 

  register(){
    this.router.navigateByUrl('/register');
  }

}
