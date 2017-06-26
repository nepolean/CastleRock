import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
@Component({
  moduleId: module.id,
  selector: 'app-account-service-upcoming',
  templateUrl: 'service-upcoming.component.html'
})
export class ServiceUpcomingComponent {

    constructor(
        private router: Router,
        private titleService: Title) {
        this.titleService.setTitle('Company | Services upcoming');
    }
}