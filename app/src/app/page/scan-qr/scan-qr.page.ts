import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { BarcodeScanner } from '@capacitor-mlkit/barcode-scanning';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-scan-qr',
  templateUrl: './scan-qr.page.html',
  styleUrls: ['./scan-qr.page.scss'],
})
export class ScanQrPage implements OnInit {

  constructor(private router: Router,private navCtrl: NavController) { }

  ngOnInit() {
    this.googleModule();
  }

  googleModule() {
    BarcodeScanner.isGoogleBarcodeScannerModuleAvailable().then(result => {
      if (!result.available) {
        BarcodeScanner.installGoogleBarcodeScannerModule().then(() => {
          console.log('Google Barcode Scanner Module installed successfully.');
        }).catch(error => {
          console.error('Error installing Google Barcode Scanner Module:', error);
        });
      } else {
        console.log('value: Google Barcode Scanner Module is already available.');
      }
    });
  }
  isGoogleBarcodeScannerModuleAvailable = async () => {
    const { available } =
      await BarcodeScanner.isGoogleBarcodeScannerModuleAvailable();
    return available;
  };

  installGoogleBarcodeScannerModule = async () => {
    await BarcodeScanner.installGoogleBarcodeScannerModule();
  };


  scanBarcode = async () => {
    try {
      const result = await BarcodeScanner['scan']();      
      if (result.barcodes != null) {       
        let scanneed = result.barcodes[0].rawValue;
        // let data = JSON.parse(scanneed);
        console.log('scanned data   ::: ',scanneed); 
        let v = localStorage.getItem('userData');
        if(v){
          this.router.navigate(['/common-page']); 
        }else{
          this.router.navigateByUrl('/register');
        }
      }
    } catch (error) {
        console.error('Barcode scanning error:', error);
    }
  };

  register(){
    this.navCtrl.navigateForward('/register');
  }
}
