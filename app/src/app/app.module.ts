import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';

import { IonicModule, IonicRouteStrategy } from '@ionic/angular';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserHomePageModule } from './page/user-home/user-home.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {  HttpClientModule } from '@angular/common/http';
import { CodeInputModule } from 'angular-code-input';
import { NativeGeocoder } from '@awesome-cordova-plugins/native-geocoder/ngx';
import { NgxSpinnerModule } from "ngx-spinner";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule,IonicModule , IonicModule.forRoot(), 
    AppRoutingModule, FormsModule,ReactiveFormsModule,HttpClientModule,BrowserAnimationsModule,
    CodeInputModule, NgxSpinnerModule],
  providers: [{ provide: RouteReuseStrategy, useClass: IonicRouteStrategy }, NativeGeocoder],
  bootstrap: [AppComponent],
})
export class AppModule {}
