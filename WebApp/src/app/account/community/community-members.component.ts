import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
@Component({
  moduleId: module.id,
  selector: 'app-account-community-members',
  styleUrls: ['community.component.css'],
  templateUrl: 'community-members.component.html'
})
export class CommunityMembersComponent {

    constructor(
        private router: Router,
        private titleService: Title) {
        this.titleService.setTitle('Company | Community members');
    }
}