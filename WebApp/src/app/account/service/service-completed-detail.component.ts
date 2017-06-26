import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-account-service-completed-detail',
  templateUrl: 'service-completed-detail.component.html'
})
export class ServiceCompletedDetailComponent {

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private titleService: Title) {
        this.titleService.setTitle('Company | Services completed');
    }
}