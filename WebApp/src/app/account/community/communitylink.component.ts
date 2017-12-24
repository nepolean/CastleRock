import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
@Component({
  moduleId: module.id,
  selector: 'app-account-communitylink',
  styleUrls: ['community.component.css'],
  templateUrl: 'communitylink.component.html'
})
export class CommunityLinkComponent {

    constructor(
        private router: Router,
        private titleService: Title) {
        this.titleService.setTitle('Company | Community');
    }
}