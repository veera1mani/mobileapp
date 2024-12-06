import { Injectable } from '@angular/core';
import { App } from '@capacitor/app';
import { AlertController, LoadingController, ToastController } from '@ionic/angular';

@Injectable({
  providedIn: 'root'
})
export class HelperService {

  constructor(public toastCtrl: ToastController,public loadingCtrl: LoadingController , public alertController: AlertController) { }


  public async presentErrorToast(msg : any) {
    const toast = await this.toastCtrl.create({
      message: msg,
      duration: 2000,
      position: 'bottom',
      // color: 'danger',
      cssClass: 'errorToastr',
      buttons: [{
        icon: 'close-circle-outline',
          side: 'start',
      }],
    });
    return toast.present();
  }


 
public async presentToast(msg: any) {
    const toast = await this.toastCtrl.create({
      message: msg,
      duration: 1500,
      position: 'bottom',
      cssClass: 'successToastr',
      buttons: [{
        icon: 'checkmark-circle-outline',
          side: 'start',
      }],
    });
    return toast.present();
  }

  public async presentNetworkToast(msg : any) {
    const toast = await this.toastCtrl.create({
      message: msg,
      duration: 0,
      position: 'bottom',
      // color: 'danger',
      cssClass: 'networkToastr',
      icon: 'globe-outline',      
    });
    return toast.present();    
  }

  async exit(msg: string) {
    const toast = await this.toastCtrl.create({
      message: msg,
      duration: 1500,
      position: 'bottom',
      mode: 'ios' ,
    });
    await toast.present();
  }

  async exit1(msg: string) {
    const toast = await this.toastCtrl.create({
      message: msg,
      duration: 1500,
      position: 'bottom',
      mode: 'ios' ,
      cssClass: 'exit',
    });
    await toast.present();
  }

  public async openLoader(msg: string){
    const loader = await this.loadingCtrl.create({
      message: msg,
      mode: 'ios',
      duration: 3000,
    });
    await loader.present();
  }

  public async closeLoader(){
    const loader = await this.loadingCtrl.create({     
      mode: 'ios',
      duration: 2000,
    });
    await loader.dismiss();
  }

  async presentAlert(msg: string) {
    const alert = await this.alertController.create({           
      message: 'Do you want to exit the app',
      mode: 'ios',
      buttons: [
        {
          text: 'CANCEL',
          role: 'cancel',
          cssClass: 'secondary',         
          handler: () => {
            console.log('cancel was clicked');
          }
        },
        {
          text: 'EXIT',
          handler: () => {
            App.exitApp();
          }
        }
      ]

    });
  
    await alert.present();
  }

  async invalidOrder(msg: string) {
    const alert = await this.alertController.create({     
      header: 'NOTICE',
      subHeader: msg , 
      mode : 'ios',
      buttons: [{
        text: 'OK',
        handler: () => {
          // this.router.navigate(['/common-dashboard/user-home']);\
          console.log('ok butotn clicked');
        }
      }]
    });
    await alert.present();
  }
}
