import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
@Component({
  moduleId: module.id,
  selector: 'app-account-community',
  styleUrls: ['community.component.css'],
  templateUrl: 'community.component.html'
})
export class CommunityComponent {

    constructor(
        private router: Router,
        private titleService: Title) {
        this.titleService.setTitle('Company | Communities');
    }
}