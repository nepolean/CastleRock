import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Paginator } from '../../common/page-utils/paginator/paginator';
import { AccountPaginatorComponent } from '../page-sections/paginator/account-paginator.component';

@Component({
  moduleId: module.id,
  selector: 'app-account-property',
  templateUrl: 'property.component.html'
})
export class PropertyComponent {

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
        this.paginator.entity = 'properties';
        this.titleService.setTitle('Company | Properties');
    }

    private handlePageSelection(paginator: Paginator): void {

    }
}