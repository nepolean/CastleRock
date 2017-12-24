import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
@Component({
  moduleId: module.id,
  selector: 'app-account-cctv',
  templateUrl: 'cctv.component.html'
})
export class CCTVComponent {

    constructor(
        private router: Router,
        private titleService: Title) {
        this.titleService.setTitle('Company | CC Tv');
    }
}