import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-common-page',
  templateUrl: './common-page.page.html',
  styleUrls: ['./common-page.page.scss'],
})
export class CommonPagePage implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }

  back(){
    this.router.navigateByUrl('/scan-qr');
  }
}
