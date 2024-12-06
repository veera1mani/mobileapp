import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NavController } from '@ionic/angular';
import { AuthService } from 'src/app/services/auth.service';
import { HelperService } from 'src/app/services/helper.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.page.html',
  styleUrls: ['./register.page.scss'],
})
export class RegisterPage implements OnInit {

  public isTextFieldType: boolean = false;

  
  registerForm!: FormGroup;
  showPassword: boolean = false;
  save:boolean=false;
  
  constructor(private fb: FormBuilder, private router: Router, 
    private helper: HelperService, private auth: AuthService,private navCtrl: NavController) {
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNo: ['', Validators.required],
    });
  }
  
  ngOnInit() {
  }

  
  togglePasswordFieldType() {
    this.isTextFieldType = !this.isTextFieldType;
  }

 
  validateInput(event: any) {
    const input = event.target as HTMLInputElement;
    if (input.value.length > 10) {
      input.value = input.value.slice(0, 10);
    }
  }

  onRegister(): void {
    this.save = true;
    console.log(this.registerForm);
    if (this.registerForm.valid) {
      console.log('Form Submitted:', this.registerForm.value);
      let value = this.registerForm.value;
      localStorage.setItem('userData', JSON.stringify(value));
      this.auth.openSpinner();
      
      this.auth.register(value).then((data: any) =>{
        this.auth.closeSpinner();
        if(data.code == '0000'){
          this.helper.presentToast('Successfully registered');
          this.navCtrl.navigateForward(['/common-page']); 
        }
      }).catch((err: any) =>{
        this.auth.closeSpinner();
        console.log(err);
      })
      
    }else
      console.log('form invalid');
      
  }

  navigateToSignIn(): void {
    this.navCtrl.navigateBack(['/scan-qr']); 
  }

 
}
