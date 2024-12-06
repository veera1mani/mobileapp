import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertController, IonModal, LoadingController, NavController, Platform } from '@ionic/angular';
import { AuthService } from 'src/app/services/auth.service';
import { HelperService } from 'src/app/services/helper.service';
import { Camera, CameraResultType, CameraSource, Photo } from '@capacitor/camera';
// import { Filesystem, Directory, Encoding } from '@capacitor/filesystem';
import { Browser } from '@capacitor/browser';
import jsPDF from 'jspdf';
import { DatePipe } from '@angular/common';
import { Subscription } from 'rxjs';


const IMAGE_DIR ='stored-images';

interface CustomBlob extends Blob {
  name: string;
}

interface Item {
  text: string;
  value: string;
}


@Component({
  selector: 'app-user-return',
  templateUrl: './user-return.page.html',
  styleUrls: ['./user-return.page.scss'],
})
export class UserReturnPage implements OnInit,OnDestroy {

  @ViewChild('modal', { static: true }) modal!: IonModal;

  @ViewChild('claimDateInput') claimDateInput!: ElementRef;

  selectedFruitsText = '0 Items';
  selectedFruits: any;

 


  newStockistId: any;
  stockistSelectionEvent(event: any , modal:IonModal){
    this.selectedFruits = event[0].stockistName +' - ' + event[0].location ;    
    console.log(event[0].stockistId );
    this.newStockistId = event[0].stockistId ; 
    modal.dismiss();
    this.stockistSelect(event[0].stockistId);
  }

  Claims!: FormGroup;
  claim!: FormGroup;
  checking!: FormGroup;
  users: any = {};
  serialNumber: any;
  returnDetail: any;
  status = false;
  returnStatus = 'created';
  transporter: any;
  stockist: any;
  selectedTransporterId: any;
  selectedStockistId: any;
  selectedClaimType: any;
  selectedManufacturName: any;

  claimNumber: any;
  claimDate: any;
  stockistId: any;
  manufac: any;
  isSecondCheck: boolean = false;
  


  submitFlag: boolean = false;
  updateButton: boolean = false;
  claimSelect: boolean = false;
  filename: any;
  uploadForm!: FormGroup;

  backButtonSubscription: Subscription = new Subscription;



  viewStatus(){
    if(this.status){
      this.status = false;
    }else{
      this.status = true;
    }
  }
  
  constructor(
    private auth: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private navCtrl: NavController,
    public helper: HelperService,
    private platform : Platform,
    private loadingCtrl: LoadingController,
    private alertController : AlertController,
    private datePipe: DatePipe
    ) 
    {
    this.Claims = this.fb.group({
      serialNumber: [null],
      claimNumber: [null, Validators.required],
      stockistId:[null],
      transporterId: [null ,Validators.required],  //
      lrNumber: [null, Validators.required],
      manufacturer: [null, Validators.required],
      lrBookingDate: [null, Validators.required],
      numberOfBoxes: [null, Validators.required],
      claimDate: [null, Validators.required],
      receivedDate: [null, Validators.required],
      returnId: [null],
    
    });
    this.claim = this.fb.group({
      serialNumber: [null],
      claimNumber:[null],      
      claimDate: [null, Validators.required],
      stockistId:[null],
      manufacturer: [null],
      transporterId: [null,Validators.required ],   //
      lrNumber: [null, Validators.required],
      lrBookingDate: [null, Validators.required],
      numberOfBoxes: [null, Validators.required],
      receivedDate: [null, Validators.required],
      returnId:[null]
    });
    this.checking = this.fb.group({
      serialNumber:  [null],
      claimNumber: [null, Validators.required],
      claimType: [null, Validators.required],
      numberOfLineItems: [null, Validators.required],
      numOfNonSalableCases:[null],
      misMatch: [null],
      misMatchType: [null],
      documentURL:new FormControl(null),
      mobileDocumentName: [null],
      remarks : [null],
      channel : [null],
      returnId:[null]
    });


    this.uploadForm = this.fb.group({
      file: [''],
    });

    
   }

   
   user: any;
  typeOfGrrn = '';
  ngOnInit() {
    this.serialNumber = this.route.snapshot.paramMap.get('ticketNo');
    this.route.queryParams.subscribe(params => {
      this.serialNumber = params['ticketNo'],
      this.typeOfGrrn = params['grrnType']
      console.log(this.serialNumber);
      console.log(this.typeOfGrrn);
    });
    this.getSerialNoDetail(this.serialNumber);  
    this.getStockists();   
    this.getTransporters();   
    
    let u : any = localStorage.getItem('etraze_user');
    this.user = JSON.parse(u);
  }

  ionViewWillEnter(){
    this.backButtonSubscription = this.platform.backButton.subscribeWithPriority(15,() =>{
      console.log('back button 6',);
        this.navCtrl.navigateBack('/common-dashboard/returns');
        // this.navCtrl.pop();
    });
  }
  ionViewWillLeave(){
    this.backButtonSubscription.unsubscribe();
  }

  ngOnDestroy(): void {
    console.log('destroy');
  }
  

  onClaimNumberChange(event:any){
    let v = event.target.value;
    console.log(v);
    this.claimNumber = v;
  }

  backNav(){
    console.log("backbutton");
    this.navCtrl.navigateRoot('/common-dashboard/returns');   //returns
  }


  claimDateChanges(e : any){
    console.log(e.target.value);
  }
  // get serial number summary
  getSerialNoDetail(number: any) {
    this.auth.openSpinner();
    this.auth.queryService('return-details/'+ number).then((data) => {
      this.auth.closeSpinner(); 
      this.returnDetail = data;
      this.checking.patchValue(this.returnDetail?.returnData);
      this.returnStatus= this.returnDetail?.returnData.status;
      this.isSecondCheck = this.returnDetail?.returnData.secondCheck;
      this.claimNumber = this.returnDetail?.returnData.claimNumber;
      this.stockistId = this.returnDetail?.returnData.stockistId;
      this.manufac = this.returnDetail?.returnData.manufacturer;
      this.checking.value.claimNumber.patchValue(this.returnDetail?.returnData.claimNumber);

      
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    })
  }
 

  // dropdown for transporter
  transportSelect(e: any) {
    this.selectedTransporterId = e.detail.value;
    console.log("transporter Id: "+this.selectedTransporterId);
  }

  // dropdown for stockist
  manufactur : any =[];
  stockistSelect(e: any) {
    this.selectedStockistId = e;   
    // for(let i=0;i<this.stk.length;i++){
    //   if(this.stk[i].stockistId===this.selectedStockistId){
    //     this.manufactur=this.stk[i].ManufactureList;        
    //   }
    // }
    this.auth.openSpinner();
    this.auth.queryService('stockist-manufacturer-list/'+ e).then((data: any) => {
      this.auth.closeSpinner();
      this.manufactur = data;
    }).then((err: any ) => {
      console.log(err);
    })

  }

  manufacturSelect(e: any) {
    this.selectedManufacturName = e.detail.value;
    console.log("manufacuture name: "+this.selectedManufacturName);
  }

  //dropdown for claim type
  claimTypeSelect(e: any) {
    this.selectedClaimType = e.detail.value;
    console.log("claim type : "+this.selectedClaimType);
    this.claimSelect = false;
  }

  customAlertOptions = {
    header: 'Stockist - Location',    
  };

  customAlertOption = {
    header: 'Manufacturer',    
  };

  customAlertOptio = {
    header: 'Transporter',    
  };

  customAlertOpti ={
    header: 'Claim Type',
  }

  customAlertOpt = {
    header: 'Mismatch Type',
  }

  
  // update serial number with received status
  received() {    
    this.submitFlag = true;
    this.Claims.value.stockistId = this.selectedStockistId;
    this.Claims.value.manufacturer = this.selectedManufacturName;
    if(this.Claims.valid){      
      this.Claims.value.transporterId = this.selectedTransporterId;
      const claimdetails = this.Claims.value ; 
      this.auth.openSpinner();
      this.auth.updateService('return-claim',claimdetails).then((data: any) => {
        this.auth.closeSpinner();
        this.users = data.data;
        if(data.code == "0000"){
          this.getSerialNoDetail(this.serialNumber);  
          this.helper.presentToast("Return received successfully....");            
          this.navCtrl.navigateRoot('/common-dashboard/returns');    
        }
      }).catch((err) => {
        this.auth.closeSpinner();
        console.log("err", err);                
      }); 
    }  
       
  }

  claimDateChanged(e: any){
    let v = e.target.value;
    console.log(v);
    this.claimDate = v;
  }

  
  // update claim with received status
  claimReceived() {       
    this.claim.value.returnId = this.returnDetail?.returnData.returnId;
    this.claim.value.claimNumber = this.claimNumber;
    this.claim.value.stockistId = this.stockistId;
    this.claim.value.manufacturer = this.manufac;  
    this.claim.value.returnId =  this.returnDetail?.returnData.returnId;
    this.submitFlag = true;

    if(this.claim.valid){      
      this.claim.value.transporterId = this.selectedTransporterId ;
      const claimdetail = this.claim.value ; 
      console.log(claimdetail);
      this.auth.openSpinner();
      this.auth.updateService('return-claim',claimdetail).then((data: any) => {
        this.auth.closeSpinner();
        this.users = data.data;
        if(data.code == "0000"){
          this.getSerialNoDetail(this.serialNumber);  
          this.helper.presentToast("Return received successfully....");            
          this.navCtrl.navigateRoot('/common-dashboard/returns');    
        }
      }).catch((err) => {
        this.auth.closeSpinner();
        console.log("err", err);                
      }); 
    }  
       
  }

  sss(evebt: any){
   console.log(evebt);
  }
  isToggleOn: boolean =false;
  onToggleChange(event: any){
    if(event.detail.checked){
      this.isToggleOn =true;
      
    } else {
      this.isToggleOn = false;
      this.mismatchType = '';
    }
  }
  mismatchType: any;
  mismatchTypeSelect(e: any){
    console.log('mismatch type : ' , e.detail.value);
    
      const concatenatedString = e.detail.value ;//const concatenatedString
      
      this.mismatchType = concatenatedString.join(', ');

      
    
  }
  nonSalableCases : string = '54';
  remarksField : string = 'adfjojdfoaijeo';

  //update ticket with checked status
  update(){ 

    if(this.docu){
      this.helper.presentErrorToast('please finish upload documents');
    }else {
      console.log('return id : ', this.returnDetail?.returnData.returnId);
      this.checking.value.returnId = this.returnDetail?.returnData.returnId;
      this.checking.value.serialNumber = this.returnDetail?.returnData.serialNumber; 
      this.checking.value.misMatch = this.isToggleOn ;
      this.checking.value.misMatchType = this.mismatchType ;
      if(this.claimNumber){
        this.checking.value.claimNumber = this.claimNumber;  
      } 
      this.checking.value.mobileDocumentName = this.newNameOfPdf ; 
      this.updateButton = true; 
      this.claimSelect = true;
       
      if((this.selectedClaimType === 'NonSaleable' || this.selectedClaimType === 'Both')&& this.checking.value.numOfNonSalableCases == null ){
        return ;
      }
      if(this.checking.valid && this.btn){
        this.checking.value.claimType = this.selectedClaimType;
        this.checking.value.documentURL = this.imageURL ; 
        this.checking.value.channel = 'HEALTHTRAZE';
        const claimdetails: any = this.checking.value;
        this.auth.openSpinner();
        this.auth.updateService('update-return-physical?type='+this.selectedClaimType,claimdetails).then((data: any) => {
          this.users = data.data;
          this.auth.closeSpinner();
          if(data.code == "0000"){
            this.getSerialNoDetail(this.serialNumber);  
            this.helper.presentToast("Return checked  successfully...."); 
            this.navCtrl.navigateRoot('/common-dashboard/returns');  
          } else if(data.code == '2222'){
            this.helper.presentErrorToast('This Claim already Checked....');
          }
        }).catch((err) => {
          this.auth.closeSpinner();
          console.log("err", err);
        }); 
      } else{
        console.log('else' , this.checking.valid);
      } 
      
    }

  }


  //get manufacturer and transporter
  //http://localhost:8080/rest/api/v1/transport-stockist
  stk : any=[];
  stockists : Item[] = [];
  getStockists() {    
    this.auth.openSpinner();
    this.auth.queryService('stickist-list').then((data:any) => { 
      this.auth.closeSpinner(); 
      this.stockists = data;
       
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    });
  }
  transports : any = [];
  getTransporters() {    
    this.auth.openSpinner();
    this.auth.queryService('transports').then((data:any) => { 
      this.auth.closeSpinner(); 
      console.log('nvjasdkvdnvav', data);
      this.transports = data;
      
    }).catch((err) => {
      this.auth.closeSpinner();
      console.log(err);
    });
  }

  checked(status: string){
    this.checking.value.serialNumber = this.returnDetail?.returnData.serialNumber; 
    this.checking.value.returnId = this.returnDetail?.returnData.returnId;
      const claimdetails: any = this.checking.value;
      this.auth.openSpinner();
      this.auth.updateService('update-return-physical?type='+status,claimdetails).then((data: any) => {
        console.log("data", data);
        this.users = data.data;
        if(data.code == "0000"){
          this.auth.closeSpinner();
          this.getSerialNoDetail(this.serialNumber);  
          this.helper.presentToast("Return checked II  "+status+"  successfully....");
          this.navCtrl.navigateRoot('/common-dashboard/returns');  
        }else {
          this.auth.closeSpinner();
        }
      }).catch((err) => {
        this.auth.closeSpinner();
        console.log("err", err);
      });      
  }

 
  imageSource: any;
  //  takePictures = async (event: any) => {
  //   console.log('event : ',event);
  //   const image = await Camera.getPhoto({
  //     quality: 90,
  //     allowEditing: false,
  //     saveToGallery: true,
  //     source: CameraSource.Prompt,
  //     resultType: CameraResultType.Base64,           
  //   }); 
    
  //   this.imageSource = CameraResultType.DataUrl;
  //   const base = image.base64String;
  //   this.fileName = 'IMG'+new Date().getTime()+'.jpg'; 
  //   this.converBase64toBlob(base ,'image/png',this.fileName );        
  // };

  // karthick 

  // converBase64toBlob(content: any, contentType: string, fileName: any) {
  //   contentType = contentType || 'image/png';
  //   const sliceSize = 512;
  //   const byteCharacters = atob(content); // Convert base64 to binary
  //   const byteArrays = [];
  //   for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
  //     const slice = byteCharacters.slice(offset, offset + sliceSize);
  //     const byteNumbers = new Array(slice.length);
  //     for (let i = 0; i < slice.length; i++) {
  //       byteNumbers[i] = slice.charCodeAt(i);
  //     }
  //     const byteArray = new Uint8Array(byteNumbers);
  //     byteArrays.push(byteArray);
  //   }
  //   let blob: any = new Blob(byteArrays, {
  //     type: contentType,
  //   });
  //   blob.filename = fileName; // Set the Blob's name property to the provided filename
  //   console.log('blob    ;:: ', blob);

  //   this.uploadForm.get("file")?.setValue(blob);
  //   this.onFileSubmit();

  //   return blob;
  

  // } 
  


  upload: boolean =  false;
  view: boolean =  false;

  onFileSelect(event: any, allowedType: string, key: string) {
    console.log("event",event.target.files[0].name);
    this.filename = event.target.files[0].name;
    this.upload = true;
    
    const file = event.target.files[0];
    var upId = file.name.split(".").pop();
    this.uploadForm.get("file")?.setValue(file);
    this.onFileSubmit();     
  }

  imageDataUrl:any = [];
  names: any = [];
  pho: any;
  docu: boolean = false;

  imagesss : any  = {
    pic: '',
    name: '',
  }

  fileName = 0  ;
  btn: boolean = false; 

  takePicture = async (event: any) => {
    console.log('event : ',event);
    Camera.requestPermissions();
    const image = await Camera.getPhoto({
      quality: 80,
      allowEditing: false,
      saveToGallery: false,
      source: CameraSource.Camera,
      resultType: CameraResultType.DataUrl,           
    });     
    this.docu = true ;
    this.btn = true;
    this.pho = image.dataUrl;

    this.fileName = this.fileName + 1 ; 
    const newImageObject = {
      pic: image.dataUrl,
      name: this.fileName ,
    }; 
    this.names.push(this.fileName);    
    this.imageDataUrl.push(newImageObject); 
    console.log(this.imageDataUrl);
    
  };

  nameOfTheFile: any;
  convertImageToPDF() {
    const A4_WIDTH = 210; // Default width of the image in millimeters
    const A4_HEIGHT = 297; // Default height of the image in millimeters
    const margin = 10;
    const doc = new jsPDF(undefined , undefined, undefined,true);

    for(let i = 0 ; i < this.imageDataUrl.length ; i++ ){
      const defaultImageWidth = A4_WIDTH - 2 * margin; // Width of the image to fit A4 page
      const defaultImageHeight = A4_HEIGHT - 2 * margin; //
      
      const imageQuality = 0.7; 
      doc.addImage(this.imageDataUrl[i].pic, 'JPEG', 
      margin,                // x coordinate
      margin,
      defaultImageWidth,     // width
      defaultImageHeight,    // height
      undefined , 
      'FAST',

      );
      if (i < this.imageDataUrl.length - 1) {
        doc.addPage(); 
      }
    }      
    const base = doc.output('blob')as CustomBlob; 
    this.nameOfTheFile = 'IMG'+new Date().getTime()+'.pdf';  
    base.name = this.nameOfTheFile;
    console.log('blob   :::::: ', base)     
    this.uploadForm.get('file')?.setValue(base);   
   
    this.onFileSubmit();  
  
  
  }

  newNameOfPdf: any;

  imageURL: string = "";
  onFileSubmit() {
    if (this.uploadForm != null && this.uploadForm.valid) {
      this.upload = true;
      const formData = new FormData();
      this.newNameOfPdf = 'new' + new Date().getTime() + '.pdf'
      formData.append("file", this.uploadForm.get("file")?.value, this.newNameOfPdf);
      this.auth.openSpinner();
      this.auth.upload_(formData).then((data: any) => {
          let dd = data;
          this.auth.closeSpinner();
          let result = dd.data;
          this.imageURL = result.fileId;
          this.checking.patchValue({
            documentUrl: result.fileId,
          });
          this.checking.value.documentURL=result.fileId;
          if(data.code == '0000'){
            this.upload = false;
            this.docu = false;
            this.helper.presentToast('Document uploaded successfully'); 
          } 
          // else {
          //   this.helper.presentErrorToast('the uploaded file too large');
          // }
        }).catch((err: any) => {
            this.auth.closeSpinner();
            this.helper.presentErrorToast('Document upload failed');
            this.upload = false;
        });
    } else if (this.uploadForm != null) {
      Object.keys(this.uploadForm.controls).forEach((field) => {
        const control = this.uploadForm.get(field);
        if (control instanceof FormControl) {
          control.markAsTouched({ onlySelf: true });
        }
      });
    }     
  }

  openDocument(){
    this.openCapacitorSite();
  }
  
  openCapacitorSite = async () => {
    await Browser.open({ url: this.imageURL });
  };
  
  deleteFiles(i: any){
    this.imageDataUrl.splice(i, 1);
  }
  res: any;
  async presentAlert() {
    const alert = await this.alertController.create({
      header: 'Alert',      
      message: 'You can either check only one at a single time',
      buttons: [
        {
          text: 'saleable',
          role: 'cancel',
          cssClass: 'secondary',
          handler: () => {
            this.res = 'saleable'
          }
        },
        {
          text: 'Non-saleable',
          handler: () => {
            this.res = 'Non-saleable'
          }
        }
      ]

    });
  
    await alert.present();
  }

  setResult(ev:any) {
    console.log(`Dismissed with role: ${ev.detail.role}`);
    
  }

  openCalendar(claimDateInput: any) {
    claimDateInput.click(); // Trigger click event of the input field
  }
}
