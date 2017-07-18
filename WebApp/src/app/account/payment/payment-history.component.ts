import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Paginator } from '../../common/page-utils/paginator/paginator';
import { AccountPaginatorComponent } from '../page-sections/paginator/account-paginator.component';

@Component({
  moduleId: module.id,
  selector: 'app-account-payment-history',
  templateUrl: 'payment-history.component.html'
})
export class PaymentHistoryComponent {

    private paginator: Paginator = new Paginator();
    constructor(
        private router: Router,
        private titleService: Title) {
        this.paginator.totalPages = 1;
        this.paginator.totalElements = 1;
        this.paginator.last = true;
        this.paginator.first = true;
        this.paginator.size = 3;
        this.paginator.currentPage = 0;
        this.paginator.numberOfElements = 1;
        this.paginator.entity = 'payments';
        this.titleService.setTitle('Company | Payment History');
    }

    private handlePageSelection(paginator: Paginator): void {

    }
}