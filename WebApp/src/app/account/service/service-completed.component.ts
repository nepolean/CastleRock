import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Paginator } from '../../common/page-utils/paginator/paginator';
import { AccountPaginatorComponent } from '../page-sections/paginator/account-paginator.component';

@Component({
  moduleId: module.id,
  selector: 'app-account-service-completed',
  templateUrl: 'service-completed.component.html'
})
export class ServiceCompletedComponent {

    private paginator: Paginator = new Paginator();
    constructor(
        private router: Router,
        private titleService: Title) {
        this.paginator.totalPages = 1;
        this.paginator.totalElements = 2;
        this.paginator.last = true;
        this.paginator.first = true;
        this.paginator.size = 2;
        this.paginator.currentPage = 0;
        this.paginator.numberOfElements = 2;
        this.paginator.entity = 'completed service jobs';
        this.titleService.setTitle('Company | Services completed');
    }

    private handlePageSelection(paginator: Paginator): void {

    }
}