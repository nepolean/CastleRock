import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router, Params } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-subscription-detail',
  templateUrl: 'subscription-detail.component.html'
})
export class SubscriptionDetailComponent implements OnInit {

    private subscriptionId: number = 1;
    constructor(
        private router: Router,
        private titleService: Title,
        private route: ActivatedRoute) {
        this.titleService.setTitle('Company | Subscriptions');
    }

    ngOnInit(): void {
        this.subscriptionId = this.route.params['id'];
        this.route.params.subscribe(params => {
            this.subscriptionId = +params['id'];
        });
    }
}