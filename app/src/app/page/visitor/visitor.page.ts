import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Camera, CameraResultType, CameraSource } from '@capacitor/camera';
import { NavController, Platform } from '@ionic/angular';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { HelperService } from 'src/app/services/helper.service';

@Component({
  selector: 'app-visitor',
  templateUrl: './visitor.page.html',
  styleUrls: ['./visitor.page.scss'],
})
export class VisitorPage implements OnInit {

  backButtonSubscription: Subscription = new Subscription; 
  vistiorForm!:FormGroup;
  uploadForm!:FormGroup;
  mobileNumber: string = '';
  submitFlag: boolean = false;
  newVisitor: boolean = false;
  visitor: boolean  = false;
  user: any;
  allVisitors: any;

  constructor(private navCtrl: NavController, private helper: HelperService,
     private platform : Platform, private fb: FormBuilder, private auth: AuthService) {

  }

  ngOnInit() {
    let u: any = localStorage.getItem('etraze_user');
    this.user = JSON.parse(u);
    this.formValidation();
    this.getAllVisitors();
  }

  formValidation(){
    this.vistiorForm = this.fb.group({
      id:[null],
      name: ['', Validators.required],
      phone: ['', Validators.required],
      emailId: ['',[Validators.required, Validators.email]],
      company:['', Validators.required],
      location:['',Validators.required],
      photoURL:[null],
      inTime:[null],
      outTime:[null],
      meetTo:['',Validators.required],
      purpose:['',Validators.required]
    });

    this.uploadForm = this.fb.group({
      file: ['']
    });
    
  }

  get emailId() {
    return this.vistiorForm.get('emailId');
  }

  ionViewWillEnter(){
    this.backButtonSubscription = this.platform.backButton.subscribeWithPriority(10, () => {
       console.log('back button 13');
       this.helper.presentAlert('Do you want to exit the app');
     });
  }

  ionViewWillLeave(){
    console.log('will leave called in back button 3');
    this.backButtonSubscription.unsubscribe();
  }

  ngOnDestroy(): void {
     console.log('destroy');
  }

  handleRefresh(event : any) {
    setTimeout(() => {
      this.getAllVisitors();
      event.target.complete();
    }, 1000);
  }

  validateInput(event: any) {
    const input = event.target as HTMLInputElement;
    if (input.value.length > 10) {
      input.value = input.value.slice(0, 10);
      this.mobileNumber = input.value;
    }else{
      this.mobileNumber = input.value;
    }
  }
  photo : any;
  getVisitor(phone?: any){
    const mobile = phone ? phone : this.mobileNumber ;
    this.vistiorForm.reset();
    this.visitor = true;
    this.auth.openSpinner();
    this.auth.queryService('visitorByPhone/'+ mobile).then((data) =>{
      this.auth.closeSpinner();
      if(data){
        this.newVisitor = false;
        this.vistiorForm.patchValue(data);
        this.vistiorForm.get('name')?.disable();
        this.vistiorForm.get('phone')?.disable();
        this.photo = this.vistiorForm.get('photoURL')?.value;
      }else{
        this.newVisitor = true;
        this.vistiorForm.get('name')?.enable();
        this.vistiorForm.get('phone')?.enable();
        this.vistiorForm.patchValue({phone:mobile});
      }
    }).catch((err)=>{
      this.auth.closeSpinner();
    });
  }

  getAllVisitors(){
    this.auth.openSpinner();
    this.auth.queryService('all-visitors').then((data) =>{
      this.auth.closeSpinner();
      this.allVisitors = data;
    }).catch((err) =>{
      this.auth.closeSpinner();
    });
  }

  save(){
    this.submitFlag = true;
    this.vistiorForm.patchValue({inTime: new Date()});
    if(this.vistiorForm.valid){
      const value = this.vistiorForm.value;
      this.auth.openSpinner();
      this.auth.createService('visitor',value).then((data: any) =>{
        this.auth.closeSpinner();
        console.log(data);
        if(data){
          this.submitFlag = false;
          this.visitor = false;
          this.imageSaved = false;
          this.image = null;
          this.vistiorForm.reset();
          this.getAllVisitors();
        }
      }).catch((err: any) =>{
        this.auth.closeSpinner();
        console.log(err);
      });
    }
  }
  update(status: any){
    if(status == 'IN') {
      this.vistiorForm.patchValue({ inTime: new Date() });
    } else {
      this.vistiorForm.patchValue({ outTime: new Date() });
    }    
    this.auth.openSpinner();
    const value = this.vistiorForm.value;
    this.auth.updateService('visitor',value).then((data: any) =>{
      this.auth.closeSpinner();
      console.log(data);
      if(data){
        this.visitor = false;
        this.vistiorForm.reset();
        this.helper.presentToast(`CHECK ${status} Successfully`);
        this.getAllVisitors();
      }
    }).catch((err)=>{
      this.auth.closeSpinner();
    });
  }

  cancel(){
    this.visitor = false;
    this.vistiorForm.reset();
    this.image = null;
    this.imageSaved = false;
    this.submitFlag = false;
  }

  image : any ;
  blob: any ;
  takePicture = async (event?: any) => {
    console.log('event : ',event);
    Camera.requestPermissions();
    const image = await Camera.getPhoto({
      quality: 100,
      allowEditing: false,
      saveToGallery: false,
      source: CameraSource.Camera,
      resultType: CameraResultType.Uri,           
    });   
    console.log(image);
    this.image =  image.webPath!;

    if (this.image) {
      const response = await fetch(this.image);
      const blob = await response.blob();
      console.log('Blob:', blob.size); 
      
      const blob1 = await this.compressImage(blob, 10);
      console.log('Compressed Blob size:', blob1.size);

      this.uploadForm.get('file')?.setValue(blob1);      
    }
  };

  compressImage = async (imageBlob: Blob, maxSizeMB: number) => {
    const maxSizeBytes = maxSizeMB * 1024 * 1024;
  
    if (imageBlob.size <= maxSizeBytes) {
      return imageBlob; // No need to compress
    }
  
    // Create an offscreen canvas and draw the image onto it
    const img = document.createElement('img');
    img.src = URL.createObjectURL(imageBlob);
  
    await new Promise((resolve) => (img.onload = resolve));
  
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d')!;
    
    // Resize the canvas to match the image
    canvas.width = img.width;
    canvas.height = img.height;
    ctx.drawImage(img, 0, 0);
  
    // Compress the image by lowering the quality
    let quality = 0.9;
    let compressedBlob = await new Promise<Blob | null>((resolve) =>
      canvas.toBlob(resolve, 'image/jpeg', quality)
    );
  
    while (compressedBlob && compressedBlob.size > maxSizeBytes && quality > 0.1) {
      quality -= 0.1;
      compressedBlob = await new Promise<Blob | null>((resolve) =>
        canvas.toBlob(resolve, 'image/jpeg', quality)
      );
    }
  
    // Return the compressed blob or the original if compression wasn't sufficient
    return compressedBlob || imageBlob;
  };


  imageSaved: boolean = false;
  saveImage(){
    const formData = new FormData();
      const newNameOfPdf = 'new' + new Date().getTime()+ '.jpg' ;
      formData.append("file",this.uploadForm.get('file')?.value , newNameOfPdf);
      this.auth.openSpinner();
      this.auth.upload_(formData).then((data: any) => {
        this.auth.closeSpinner();
        console.log('data :::: ', data);
        if(data.code == '0000'){
          this.helper.presentToast('Photo uploaded successfully'); 
          this.uploadForm.reset();
          this.imageSaved = true;
          this.vistiorForm.get('photoURL')?.setValue(data?.data.url);
          console.log('visitor form . photo url , ' , this.vistiorForm.get('photoURL'));
        }
      }).catch((err: any) => {
          this.auth.closeSpinner();
          this.helper.presentErrorToast('Photo upload failed');
      });
      
  }
}


