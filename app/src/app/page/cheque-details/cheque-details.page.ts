import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LoadingController, NavController, Platform } from '@ionic/angular';
import { AuthService } from 'src/app/services/auth.service';
import { Camera, CameraResultType, CameraSource,ImageOptions, GalleryImageOptions} from "@capacitor/camera"
import { HelperService } from 'src/app/services/helper.service';
import { Browser } from '@capacitor/browser';
import jsPDF, * as jspdf from 'jspdf';
// import 'jspdf-autotable';
import { Capacitor,Plugins } from '@capacitor/core';
import { PDFDocument } from 'pdf-lib';


const { Filesystem } = Plugins;
import * as pdfMake from "pdfmake/build/pdfmake";
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import { buffer } from 'rxjs-compat/operator/buffer';

interface CustomBlob extends Blob {
  name: string;
}
(<any>pdfMake).vfs = pdfFonts.pdfMake.vfs;
@Component({
  selector: 'app-cheque-details',
  templateUrl: './cheque-details.page.html',
  styleUrls: ['./cheque-details.page.scss'],
})

export class ChequeDetailsPage implements OnInit {

  backButtonSubscription: any;
  status = false;
  chequeId: any;
  chequeDetail: any;
  stockistId: any;

  imgs: string[] = [];
  files: any;

  base64: any;
  uploadForm!: FormGroup;

  pdfObj: any;

  constructor(public platform:Platform, 
    private auth: AuthService,
    private navCtrl: NavController ,private helper: HelperService, private router: Router, private route: ActivatedRoute, 
    private loader: LoadingController, private fb: FormBuilder) 
    {
      this.uploadForm = this.fb.group({
        file: [''],
      });
    }

  ngOnInit() {
    
    console.log('init');
    // if(window.confirm("do you want to pick images ?")){
    //   this.pickImages();
    // }

    Camera.requestPermissions({permissions: ['photos']});
  }

  // pickImages(){
  //   this.loader.create({
  //     message: '  please wait... '
  //   }).then((ele) => {
  //     ele.present();
  //     let options : GalleryImageOptions ={
  //       correctOrientation: true,                
  //     } 
       
  //     Camera.pickImages(options).then((val: { photos: any; }) => {
  //       var images = val.photos;
  //       this.imgs = [];
  //       for(let i=0; i< images.length ; i++){
  //         this.imgs.push(images[i].webPath);
  //       }
  //       ele.dismiss();
  //     })
  //   })
  // }
  
  ngOnDestroy() {
    console.log('onDestroy');
    
  }

  backNav(){
    this.navCtrl.navigateBack('/common-dashboard/user-home');
  }
  //////////////

  imageSource: any;
  imageBlob:any = [];
  imageDataUrl:any = [];
  pic: any;
  pho: any;
  pdfBlob: any ; 

  takePicture = async (event: any) => {
    console.log('event : ',event);
    const image = await Camera.getPhoto({
      quality: 80,
      allowEditing: false,
      saveToGallery: false,
      source: CameraSource.Prompt,
      resultType: CameraResultType.DataUrl,           
    });     
 
    this.pho = image.dataUrl;
    
    console.log('data url : ', this.pho);
    this.imageDataUrl.push(this.pho);  
  };

  convertImageToPDF() {
    const A4_WIDTH = 210; // Default width of the image in millimeters
    const A4_HEIGHT = 297; // Default height of the image in millimeters
    const margin = 10;
    const doc = new jsPDF(undefined , undefined, undefined,true);

    for(let i = 0 ; i < this.imageDataUrl.length ; i++ ){
      const defaultImageWidth = A4_WIDTH - 2 * margin; // Width of the image to fit A4 page
      const defaultImageHeight = A4_HEIGHT - 2 * margin; // 

      doc.addImage(this.imageDataUrl[i], 'JPEG', 
      margin,                // x coordinate
      margin,
      defaultImageWidth,     // width
      defaultImageHeight ,'','FAST'    // height
      );

      // doc.addImage(this.imageDataUrl[i], 'PNG', 10, 120, 485, 270,'','FAST');
      if (i < this.imageDataUrl.length - 1) {
        doc.addPage(); 
      }
    }   
    
    doc.save('new Pdf');
    console.log('size of the pdf ', doc.output().length);
    const base = doc.output('blob')as CustomBlob; 
    const fileName = 'IMG'+new Date().getTime()+'.pdf';  
    base.name = fileName;
    console.log('blob   :::::: ', base)     
    this.uploadForm.get('file')?.setValue(base);   
   
    this.onFileSubmit();  
  
  
  }
  generatePDF() {
    if (!this.imageBlob) {
      console.error("No image captured.");
      this.helper.presentErrorToast('no image captured');
      return;
    }
    const img = new Image();
    img.onload = async () => {
      const doc = new jspdf.jsPDF();
      const canvas = document.createElement('canvas');
      const ctx = canvas.getContext('2d');
      canvas.width = img.width;
      canvas.height = img.height;
      ctx?.drawImage(img, 0, 0);
      const dataURI = canvas.toDataURL('image/jpeg');
      doc.addImage(dataURI, 'JPEG', 10, 10, 100, 100);
      const pdfData = doc.output('arraybuffer'); // Convert to ArrayBuffer
      const pdfFileName = 'image_to_pdf.pdf';
      // await this.saveAndOpenPDF(pdfData, pdfFileName);
    };
    img.src = this.imageBlob;
    this.helper.presentToast('inside generate pdf method');
  } 


  converBase64toBlob(content: any, contentType: string, fileName: any) {
    contentType = contentType || 'image/png';
    const sliceSize = 512;
    const byteCharacters = atob(content); // Convert base64 to binary
    const byteArrays = [];
    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      const slice = byteCharacters.slice(offset, offset + sliceSize);
      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }
    let blob: any = new Blob(byteArrays, {
      type: contentType,
    });
    blob.name = fileName; // Set the Blob's name property to the provided filename
    

    this.uploadForm.get("file")?.setValue(blob);
    // this.onFileSubmit();
    
    return blob;
  } 

  openDocument(){
    this.openCapacitorSite();
  }
  
  openCapacitorSite = async () => {
    await Browser.open({ url: this.imageUrl });
  };

  deleteFiles(i: any){
    this.imageDataUrl.splice(i, 1);
  }

 dd: any;
 view : boolean = false ;
 imageUrl = '';
//   onFileSubmit() {
//     if(this.uploadForm != null && this.uploadForm.valid){   
//       const file: File | null = this.uploadForm.get("file")?.value;
//       if (file) {
//           console.log('File name:', file.name); 
//       const formData = new FormData();
//       formData.append("file", this.uploadForm.get("file")?.value);
//       console.log('form data :  , ', formData);
      
//       this.auth.upload_(formData).then((data: any) => {
//           this.dd = data;
//           let result = this.dd.data;
//           this.imageUrl = result.url;
//           if(data.code == '0000'){
//             this.view= true;
//           //  this.navCtrl.navigateBack('/common-dashboard/user-home');
//             this.helper.presentToast('Document uploaded successfully');          
//           }
//         }).catch((err: any) => {
//             this.helper.presentErrorToast('Document Upload Failed');
          
//         });
//     } else {
//       this.helper.presentErrorToast('No Images Selected');
//     }
    
//   }  
// }
onFileSubmit() {
  if (this.uploadForm != null && this.uploadForm.valid) {
      const file: File | null = this.uploadForm.get("file")?.value;
      if (file) {
          console.log('File name:', file.name); // View the filename
          const formData = new FormData();
          formData.append("file", file,file.name);
          // formData.append("file", file);
          // formData.append("name", file.name); // Set the filename as a parameter

          console.log('form data :  , ', formData);

          this.auth.upload_(formData).then((data: any) => {
              this.dd = data;
              let result = this.dd.data;
              this.imageUrl = result.url;
              if (data.code == '0000') {
                  this.view = true;
                  //  this.navCtrl.navigateBack('/common-dashboard/user-home');
                  this.helper.presentToast('Document uploaded successfully');
              }
          }).catch((err: any) => {
              this.helper.presentErrorToast('Document Upload Failed');

          });
      } else {
          console.error('No file selected');
          this.helper.presentErrorToast('No Images Selected');
      }
  }
}

}
