import { Component, NgZone, OnInit } from '@angular/core';
import { SplashScreen } from '@capacitor/splash-screen';
import { HelperService } from './services/helper.service';
import { ToastController } from '@ionic/angular';
import { Capacitor, PluginListenerHandle, Plugins } from '@capacitor/core';
// import { Network } from '@capacitor/network';
import { Device } from '@capacitor/device';
// import { Geolocation } from '@capacitor/geolocation';


const { Permissions } = Plugins;

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent implements OnInit {
  status: any;
  networkListener: PluginListenerHandle | undefined;

  constructor(private helper: HelperService, private toastController: ToastController, private ngZone: NgZone) 
  {
   

  
   
  }

  


  async ngOnInit() {
    // await SplashScreen.show({
    //   autoHide: true,
    //   showDuration: 1500,
    // });

    await SplashScreen.hide();

    // this.networkListener = await Network.addListener('networkStatusChange', status => {
    //   this.ngZone.run(()=>{ 
    //     // this.changeStatus(status);
    //     this.toastController.dismiss();
    //   })
    // });    
      // const status = await Network.getStatus();     
      // this.changeStatus(status);  
      
      this.logDeviceId();
  }
  
  changeStatus(status: any){
    this.status = status?.connected;
    console.log('Network : ', this.status);
    if(!this.status){     
      // this.helper.presentNetworkToast(" Seems like you are offline...");
      console.log('inside if present error toast ');
    }      
    else{ 
      console.log('inside else present alert');
    }    
  }

  logDeviceId = async () => {
    const info = await Device.getId();
    localStorage.setItem("device_id", info.identifier);
  }

  
  
}
