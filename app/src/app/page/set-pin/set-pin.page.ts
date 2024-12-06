import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';
import { error } from 'highcharts';
import { HelperService } from 'src/app/services/helper.service';
import { Device } from '@capacitor/device';
import { AuthService } from 'src/app/services/auth.service';


@Component({
  selector: 'app-set-pin',
  templateUrl: './set-pin.page.html',
  styleUrls: ['./set-pin.page.scss'],
})
export class SetPinPage implements OnInit {

  role: any;
  pin1: string='';
  pin2: string='';
  device: any;
  tenantId: any;
  user:any;

  deviceId: any;

  createPin:any =  {
    userId: '',
    pin: '',
    deviceId: '', 
    tenantId: ''
  }

  constructor(private router: Router, private navCtrl: NavController,private helper:HelperService,private fb: FormBuilder, private auth: AuthService) 
  {
    
  }

  ngOnInit(): void {
    let u: any = localStorage.getItem('etraze_user');
    this.user = JSON.parse(u);
    this.role = localStorage.getItem('user_role');    
    this.logDeviceId();   
    this.deviceId = localStorage.getItem('device_id');
  }

  RouteToLogin() {  
    this.router.navigate(['/login-page']);
  }

  creatPinStatus : any=false;
  createPinStatus: any = false;

  GoToHomeModule(){
   
    if(this.firstField != this.second){
      this.helper.presentErrorToast('confirm pin not matched');
      return
    } else if(this.second == null || this.second == undefined || this.second == ''){
      this.helper.presentErrorToast('Enter confirm pin');
      return
    }
    this.createPin.pin = this.firstField;
    this.createPin.deviceId = this.device;
    this.createPin.tenantId = this.user.tenantId;
    this.createPin.userId = this.user.userId ; 
    this.auth.openSpinner();
    this.auth.createService('mobile-pin',this.createPin).then((data: any) => {
      if(data.code == "0000"){
        this.auth.closeSpinner();
        if(this.role == "TRANSPORT"){
          this.navCtrl.navigateRoot(['/common-dashboard/transporter']);
        }
        else if(this.role == "MANAGER"){
        this.navCtrl.navigateRoot('/common-dashboard/home');
        }
        else if(this.role == "SUPERADMIN"){
        this.navCtrl.navigateRoot('/common-dashboard/o-orders');
        }
        else if(this.role == "USER"){
         this.navCtrl.navigateRoot('/common-dashboard/user-home');
        }
        else if(this.role === 'SECURITY'){
          this.navCtrl.navigateRoot('/visitor');                  
        }
        else{
         this.helper.presentErrorToast(this.role+' this user is not available for mobile')    
        }
        this.helper.presentToast("Pin created successfully");
      }
      else if(data.code == '1111') {
        this.auth.closeSpinner();
        this.helper.presentErrorToast('This pin already exists');
      }
    }).catch((err) => { 
      this.auth.closeSpinner();
      this.helper.presentErrorToast("Please enter valid credentials");
      console.log("err", err);      
    });

    
    

  }

  logDeviceId = async () => {
    const info = await Device.getId();
    this.device = info.identifier;
    localStorage.setItem("device_id", this.device);
   
  }

  userModule(){
    this.navCtrl.navigateRoot('/common-dashboard/user-home');    
  }
  
  
  create: any;
  confirm: any;
  yourModel: string = '';
  firstField: any;
  secondField: boolean = false;
  second: any ;

  onCodeCompleted(code: string) {
    console.warn('code ', code);
    this.firstField = code;
  }

  onCodeChange(code: string){
    this.second = code ;
    if(code != null && code == this.firstField  ){
      this.secondField = false;
    } else if(code == null || code == undefined || code == '') {
      this.secondField = false;
    } else {
      this.secondField = true ;
    }
  }
  onCodeComplete(code: string){
    if(code == this.firstField){
      this.secondField = false;
      this.second = code ;
    } else {
      this.secondField = true;
    }
  }
}
