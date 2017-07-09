import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router, Params } from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-payment-success',
  templateUrl: 'payment-success.component.html'
})
export class PaymentSuccessComponent implements OnInit {

    private paymentId: number = 1;
    constructor(
        private router: Router,
        private titleService: Title,
        private route: ActivatedRoute) {
        this.titleService.setTitle('Company | Payment Detail');
    }

    ngOnInit(): void {
        this.paymentId = this.route.params['id'];
        this.route.params.subscribe(params => {
            this.paymentId = +params['id'];
        });
    }
}